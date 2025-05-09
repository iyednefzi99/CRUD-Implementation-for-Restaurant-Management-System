<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="entites.accounts_package.Account"%>
<%@page import="entites.accounts_package.AccountStatus"%>
<%@page import="java.util.logging.Logger"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    final Logger logger = Logger.getLogger(this.getClass().getName());
    
    Account account = (Account) request.getAttribute("account");
    boolean isEditMode = account != null;
    String pageTitle = isEditMode ? "Edit Account" : "Create New Account";
    
    // Initialize form values
    String username = "";
    String email = "";
    String status = "ACTIVE";
    
    if (isEditMode) {
        username = account.getUsername() != null ? account.getUsername() : "";
        email = account.getEmail() != null ? account.getEmail() : "";
        status = account.getStatus() != null ? account.getStatus().name() : "ACTIVE";
    }
    
    // Store in request attributes for JSTL access
    request.setAttribute("isEditMode", isEditMode);
    request.setAttribute("pageTitle", pageTitle);
    request.setAttribute("username", username);
    request.setAttribute("email", email);
    request.setAttribute("status", status);
%>
<%@include file="/templates/header.jsp" %>

<div class="container mt-4">
    <h1>${pageTitle}</h1>
    
    <%-- Error message display --%>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            ${error}
        </div>
    </c:if>
    
    <form action="${contextPath}/account/${isEditMode ? 'update' : 'insert'}" method="post" class="needs-validation" novalidate>
        <c:if test="${isEditMode}">
            <input type="hidden" name="username" value="${username}">
        </c:if>
        
        <div class="mb-3">
            <label for="username" class="form-label">Username</label>
            <input type="text" class="form-control ${isEditMode ? 'bg-light' : ''}" 
                   id="username" name="username" 
                   value="${username}" 
                   ${isEditMode ? 'readonly' : 'required'}>
            <div class="invalid-feedback">
                Please provide a username.
            </div>
        </div>
        
        <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input type="password" class="form-control" id="password" 
                   name="password" ${isEditMode ? '' : 'required'}
                   placeholder="${isEditMode ? 'Leave blank to keep current password' : ''}">
            <div class="invalid-feedback">
                ${isEditMode ? 'Leave blank to keep current password' : 'Please provide a password'}
            </div>
        </div>
        
        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" 
                   name="email" value="${email}" required>
            <div class="invalid-feedback">
                Please provide a valid email.
            </div>
        </div>
        
        <div class="mb-3">
            <label for="status" class="form-label">Status</label>
            <select class="form-select" id="status" name="status" required>
                <c:forEach items="<%=AccountStatus.values()%>" var="st">
                    <option value="${st.name()}" ${status eq st.name() ? 'selected' : ''}>
                        ${st.name()}
                    </option>
                </c:forEach>
            </select>
        </div>
        
        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
            <a href="${contextPath}/account/list" class="btn btn-secondary me-md-2">
                <i class="bi bi-x-circle"></i> Cancel
            </a>
            <button type="submit" class="btn btn-primary">
                <i class="bi bi-save"></i> Save
            </button>
        </div>
    </form>
</div>

<script>
// Client-side form validation
(function() {
    'use strict';
    window.addEventListener('load', function() {
        var forms = document.getElementsByClassName('needs-validation');
        Array.prototype.filter.call(forms, function(form) {
            form.addEventListener('submit', function(event) {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    }, false);
})();

// Password confirmation for new accounts
<c:if test="${not isEditMode}">
document.getElementById('password').addEventListener('input', function() {
    if (this.value.length < 6) {
        this.setCustomValidity('Password must be at least 6 characters');
    } else {
        this.setCustomValidity('');
    }
});
</c:if>
</script>

<%@include file="/templates/footer.jsp"%>