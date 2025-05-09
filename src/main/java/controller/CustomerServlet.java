package controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import dao.AccountDAO;
import dao.CustomerDAO;
import entites.accounts_package.Account;
import entites.customers_package.Customer;
import entites.customers_package.VIPCustomer;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/customer/*")
public class CustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    public void init() {
        customerDAO = new CustomerDAO();
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
                    insertCustomer(request, response);
                    break;
                case "/delete":
                    deleteCustomer(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateCustomer(request, response);
                    break;
                case "/list":
                default:
                    listCustomers(request, response);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Customer> customers = customerDAO.getAll();
        request.setAttribute("customers", customers);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/customer/customerlist.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Account> accounts = accountDAO.getAll();
        request.setAttribute("accounts", accounts);
        request.setAttribute("isVip", false);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/customer/customerform.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String id = request.getParameter("id");
        Customer existingCustomer = customerDAO.get(id)
                .orElseThrow(() -> new Exception("Customer not found with id: " + id));
        List<Account> accounts = accountDAO.getAll();
        
        request.setAttribute("customer", existingCustomer);
        request.setAttribute("accounts", accounts);
        request.setAttribute("isVip", existingCustomer instanceof VIPCustomer);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/customer/customerform.jsp");
        dispatcher.forward(request, response);
    }

    private void insertCustomer(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String accountUsername = request.getParameter("accountUsername");
        boolean isVip = "on".equals(request.getParameter("isVip"));
        
        Account account = accountDAO.get(accountUsername)
                .orElseThrow(() -> new Exception("Account not found"));
        
        Customer newCustomer;
        if (isVip) {
            int clientDiscount = Integer.parseInt(request.getParameter("clientDiscount"));
            newCustomer = new VIPCustomer(id, name, phone, 
                account.getUsername(), account.getPassword(), account.getEmail(),
                new Date(), clientDiscount);
        } else {
            newCustomer = new Customer(id, name, phone, 
                account.getUsername(), account.getPassword(), account.getEmail(),
                new Date());
        }

        if (customerDAO.save(newCustomer)) {
            response.sendRedirect("list");
        } else {
            throw new Exception("Failed to insert customer");
        }
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String accountUsername = request.getParameter("accountUsername");
        boolean isVip = "on".equals(request.getParameter("isVip"));
        
        Account account = accountDAO.get(accountUsername)
                .orElseThrow(() -> new Exception("Account not found"));
        
        Customer customer;
        if (isVip) {
            int clientDiscount = Integer.parseInt(request.getParameter("clientDiscount"));
            customer = new VIPCustomer(id, name, phone, 
                account.getUsername(), account.getPassword(), account.getEmail(),
                new Date(), clientDiscount);
        } else {
            customer = new Customer(id, name, phone, 
                account.getUsername(), account.getPassword(), account.getEmail(),
                new Date());
        }

        if (customerDAO.update(customer)) {
            response.sendRedirect("list");
        } else {
            throw new Exception("Failed to update customer");
        }
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String id = request.getParameter("id");
        if (customerDAO.delete(id)) {
            response.sendRedirect("list");
        } else {
            throw new Exception("Failed to delete customer");
        }
    }
}