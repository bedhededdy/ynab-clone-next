package com.ynab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ynab.model.Budget;
import com.ynab.model.User;
import com.ynab.repository.BudgetRepository;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserService userService;

    public List<Budget> getBudgetsForUser(Long userId) {
        User user = userService.getUserById(userId);
        if (user == null)
            return null;
        return user.getBudgets();
    }

    public Budget getBudgetById(Long budgetId) {
        return budgetRepository.findById(budgetId).orElse(null);
    }

    public Budget createBudgetForUser(Long userId, String budgetName) {
        User user = userService.getUserById(userId);
        if (user == null)
            return null;
        Budget budget = budgetRepository.findByName(budgetName);
        if (budget != null)
            return null;
        budget = new Budget(budgetName, user);
        user.getBudgets().add(budget);
        budget = budgetRepository.save(budget);
        return budget;
    }
}
