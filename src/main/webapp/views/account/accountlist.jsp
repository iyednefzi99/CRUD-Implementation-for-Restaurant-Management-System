<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="entites.accounts_package.Account"%>
<%@page import="entites.accounts_package.AccountStatus"%>
<%@page import="dao.AccountDAO"%>
<%
AccountDAO accountDAO = new AccountDAO();
List<Account> accounts = accountDAO.getAll();
request.setAttribute("accounts", accounts);

@SuppressWarnings("unchecked")
List<Account> Accounts = (List<Account>) request.getAttribute("accounts");
if (accounts == null) {
    accounts = Collections.emptyList(); // Immutable empty list
}
if (request.getAttribute("accounts") == null) { 
    response.sendRedirect("account/list");
    return;
}
%>
<%@include file="/templates/header.jsp" %>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">
            <i class="bi bi-people me-2"></i>Account Management
        </h2>
        <a href="${pageContext.request.contextPath}/views/account/accountform.jsp" class="btn btn-primary">
            <i class="bi bi-plus-circle me-1"></i>Add New Account
        </a>
    </div>
    
    <% if (request.getAttribute("success") != null) { %>
        <div class="alert alert-success auto-dismiss fade show" role="alert">
            <i class="bi bi-check-circle me-2"></i><%= request.getAttribute("success") %>
        </div>
    <% } %>
    
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-danger auto-dismiss fade show" role="alert">
            <i class="bi bi-exclamation-triangle me-2"></i><%= request.getAttribute("error") %>
        </div>
    <% } %>
    
    <div class="card shadow-sm">
        <div class="card-body">
            <% if (accounts == null || accounts.isEmpty()) { %>
                <div class="alert alert-info">
                    <i class="bi bi-info-circle me-2"></i>No accounts found
                </div>
            <% } else { %>
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Account account : accounts) { %>
                                <tr>
                                    <td><%= account.getUsername() %></td>
                                    <td><%= account.getEmail() %></td>
                                    <td>
                                        <span class="badge <%= getStatusBadgeClass(account.getStatus()) %>">
                                            <%= account.getStatus() %>
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <div class="btn-group btn-group-sm" role="group">
                                            <a href="<%= contextPath %>/account/edit?username=<%= account.getUsername() %>" 
                                               class="btn btn-outline-primary"
                                               data-bs-toggle="tooltip" title="Edit">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <a href="<%= contextPath %>/account/delete?username=<%= account.getUsername() %>" 
                                               class="btn btn-outline-danger confirm-before-action"
                                               data-confirm-message="Are you sure you want to delete this account?"
                                               data-bs-toggle="tooltip" title="Delete">
                                                <i class="bi bi-trash"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } %>
        </div>
    </div>
</div>

<%!
    private String getStatusBadgeClass(AccountStatus status) {
        if (status == null) return "bg-secondary";
        
        switch(status) {
            case ACTIVE: return "bg-success";
            case INACTIVE: return "bg-warning text-dark";
            case SUSPENDED: return "bg-danger";
            default: return "bg-secondary";
        }
    }
%>

<%@include file="/templates/footer.jsp"%>