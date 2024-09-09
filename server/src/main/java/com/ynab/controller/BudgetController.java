package com.ynab.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynab.model.Budget;
import com.ynab.model.User;
import com.ynab.service.BudgetService;
import com.ynab.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    @Autowired
    private UserService userService;

    @Data
    private static class CreateBudgetRequest {
        private String budgetName;
    }

    @Data
    private static class BudgetResponse {
        private Long budgetId;
        private String budgetName;

        public BudgetResponse(Budget budget) {
            this.budgetId = budget.getId();
            this.budgetName = budget.getName();
        }
    }

    private static boolean budgetBelongsToAuthenticatedUser(User userId, Budget budget) {
        return userId.getId().equals(budget.getUser().getId());
    }

    @PostMapping("/createBudget")
    public ResponseEntity<BudgetResponse> createBudget(@RequestBody CreateBudgetRequest req, HttpServletRequest request) {
        /* *ECP FIXME: NEED THE BUDGET NAME TO BE UNIQUE FOR THE USER */
        User user = userService.getUserFromRequest(request);
        if (user == null)
            return ResponseEntity.badRequest().build();
        Budget budget = budgetService.createBudgetForUser(user.getId(), req.getBudgetName());
        if (budget == null)
            return ResponseEntity.badRequest().build(); // FIXME: Should be different if the budget already existed vs failure
        return ResponseEntity.ok(new BudgetResponse(budget));
    }

    @GetMapping("/getBudget")
    public ResponseEntity<BudgetResponse> getBudget(@RequestParam Long budgetId, HttpServletRequest request) {
        // FIXME: THE BUDGET SERVICE IS RE-FETCHING THE USER FROM DB
        User user = userService.getUserFromRequest(request);
        if (user == null)
            return ResponseEntity.badRequest().build();
        Budget budget = budgetService.getBudgetById(budgetId);
        if (!budgetBelongsToAuthenticatedUser(user, budget))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        if (budget == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new BudgetResponse(budget));
    }

    @GetMapping("/getBudgetList")
    public ResponseEntity<List<BudgetResponse>> getBudgetList(HttpServletRequest request) {
        // FIXME: THE BUDGET SERVICE IS RE-FETCHING THE USER FROM DB
        User user = userService.getUserFromRequest(request);
        if (user == null)
            return ResponseEntity.badRequest().build();
        List<Budget> budgets = budgetService.getBudgetsForUser(user.getId());
        if (budgets == null || budgets.isEmpty())
            return ResponseEntity.notFound().build();
        List<BudgetResponse> budgetResponses = budgets.stream()
            .map(BudgetResponse::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(budgetResponses);
    }

    @PutMapping("/updateBudget")
    public ResponseEntity<Budget> updateBudget() {
        return null;
    }

    @PostMapping("/deleteBudget")
    public ResponseEntity<Budget> deleteBudget() {
        return null;
    }
}
