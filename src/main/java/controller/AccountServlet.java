package controller;

import java.io.IOException;
import java.util.List;

import dao.AccountDAO;
import entites.accounts_package.Account;
import entites.accounts_package.AccountStatus;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/account/*")
public class AccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AccountDAO accountDAO;

    public void init() {
        accountDAO = new AccountDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo() == null ? "/list" : request.getPathInfo();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertAccount(request, response);
                    break;
                case "/delete":
                    deleteAccount(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateAccount(request, response);
                    break;
                case "/list":
                default:
                    listAccounts(request, response);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
        }
    }

    private void listAccounts(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Account> accounts = accountDAO.getAll();
        request.setAttribute("accounts", accounts);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/account/accountlist.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/account/accountform.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String username = request.getParameter("username");
        Account existingAccount = accountDAO.get(username)
                .orElseThrow(() -> new Exception("Account not found with username: " + username));
        request.setAttribute("account", existingAccount);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/account/accountform.jsp");
        dispatcher.forward(request, response);
    }

    private void insertAccount(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String status = request.getParameter("status");

        Account newAccount = new Account(username, password, email);
        newAccount.setStatus(AccountStatus.valueOf(status));

        if (accountDAO.save(newAccount)) {
            response.sendRedirect("list");
        } else {
            throw new Exception("Failed to insert account");
        }
    }

    private void updateAccount(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String status = request.getParameter("status");

        Account account = new Account(username, password, email);
        account.setStatus(AccountStatus.valueOf(status));

        if (accountDAO.update(account)) {
            response.sendRedirect("list");
        } else {
            throw new Exception("Failed to update account");
        }
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String username = request.getParameter("username");
        if (accountDAO.delete(username)) {
            response.sendRedirect("list");
        } else {
            throw new Exception("Failed to delete account");
        }
    }
}