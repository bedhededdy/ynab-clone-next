import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import Page from "@/app/login/page";
import { useRouter } from "next/navigation";

// Mock the useRouter hook
jest.mock("next/navigation", () => ({
  useRouter: jest.fn(),
}));

describe("Login Page", () => {
  const mockPush = jest.fn();

  beforeEach(() => {
    (useRouter as jest.Mock).mockReturnValue({
      push: mockPush,
    });
  });

  it("Requires an email and password to be entered", async () => {
    render(<Page />);

    // Submit the form
    fireEvent.click(screen.getByRole("button", { name: /login/i }));

    // Wait for the error message to appear
    await waitFor(() => {
      expect(screen.getAllByText(/invalid email/i)).toHaveLength(1);
      expect(screen.getAllByText(/must contain at least/i)).toHaveLength(1);
    });
  });

  it("Displays errors when the login is invalid", async () => {
    // Mock the fetch API to return a failed response
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: false,
        status: 403,
      } as Response)
    );

    render(<Page />);

    // Simulate user input
    fireEvent.change(screen.getByLabelText(/email/i), {
      target: { value: "test@example.com" },
    });
    fireEvent.change(screen.getByLabelText(/password/i), {
      target: { value: "password123" },
    });

    // Submit the form
    fireEvent.click(screen.getByRole("button", { name: /login/i }));

    // Wait for the error messages to appear
    await waitFor(() => {
      expect(screen.getAllByText(/invalid email or password/i)).toHaveLength(2);
    });
  });

  it("Displays a generic error message when the server is down", async () => {
    // Mock the fetch API to return a failed response
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: false,
        status: 500,
      } as Response)
    );

    render(<Page />);

    // Simulate user input
    fireEvent.change(screen.getByLabelText(/email/i), {
      target: { value: "test@example.com" },
    });

    fireEvent.change(screen.getByLabelText(/password/i), {
      target: { value: "password123" },
    });

    // Submit the form
    fireEvent.click(screen.getByRole("button", { name: /login/i }));

    // Wait for the error message to appear
    await waitFor(() => {
      expect(screen.getAllByText(/server error/i)).toHaveLength(2);
    });
  });

  it("Redirects to budget-select on successful login", async () => {
    // Mock the fetch API to return a successful response
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve({}),
      } as Response)
    );

    render(<Page />);

    // Simulate user input
    fireEvent.change(screen.getByLabelText(/email/i), {
      target: { value: "test@example.com" },
    });
    fireEvent.change(screen.getByLabelText(/password/i), {
      target: { value: "password123" },
    });

    // Submit the form
    fireEvent.click(screen.getByRole("button", { name: /login/i }));

    // Wait for the router push to be called
    await waitFor(() => {
      expect(mockPush).toHaveBeenCalledWith("/budget-select");
    });
  });
});
