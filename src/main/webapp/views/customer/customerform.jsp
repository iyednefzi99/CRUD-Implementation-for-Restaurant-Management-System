<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="entites.customers_package.Customer"%>
<%@page import="entites.customers_package.VIPCustomer"%>
<%@page import="java.util.List"%>
<%@page import="entites.accounts_package.Account"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="/templates/header.jsp" %>

<div class="container mt-4">
    <h1>${customer == null ? 'Create New Customer' : 'Edit Customer'}</h1>
    
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    
    <form action="${contextPath}/customer/${customer == null ? 'insert' : 'update'}" method="post" class="needs-validation" novalidate>
        <c:if test="${customer != null}">
            <input type="hidden" name="id" value="${customer.id}">
        </c:if>
        
        <div class="mb-3">
            <label for="id" class="form-label">Customer ID</label>
            <input type="text" class="form-control ${customer != null ? 'bg-light' : ''}" 
                   id="id" name="id" value="${customer.id}" 
                   ${customer != null ? 'readonly' : 'required'}>
            <div class="invalid-feedback">Please provide a customer ID.</div>
        </div>
        
        <div class="mb-3">
            <label for="name" class="form-label">Name</label>
            <input type="text" class="form-control" id="name" name="name" 
                   value="${customer.name}" required>
            <div class="invalid-feedback">Please provide a name.</div>
        </div>
        
        <div class="mb-3">
            <label for="phone" class="form-label">Phone</label>
            <input type="tel" class="form-control" id="phone" name="phone" 
                   value="${customer.phone}" required>
            <div class="invalid-feedback">Please provide a phone number.</div>
        </div>
        
        <div class="mb-3">
            <label for="accountUsername" class="form-label">Account</label>
            <select class="form-select" id="accountUsername" name="accountUsername" required>
                <option value="">Select an account</option>
                <c:forEach items="${accounts}" var="account">
                    <option value="${account.username}" 
                        ${customer.account.username eq account.username ? 'selected' : ''}>
                        ${account.username} (${account.email})
                    </option>
                </c:forEach>
            </select>
        </div>
        
        <div class="mb-3 form-check">
            <input type="checkbox" class="form-check-input" id="isVip" name="isVip" 
                   ${isVip ? 'checked' : ''}>
            <label class="form-check-label" for="isVip">VIP Customer</label>
        </div>
        
        <div class="mb-3" id="discountContainer" style="${isVip ? '' : 'display: none;'}">
            <label for="clientDiscount" class="form-label">Discount (%)</label>
            <input type="number" class="form-control" id="clientDiscount" name="clientDiscount" 
                   min="0" max="50" value="${customer instanceof VIPCustomer ? ((VIPCustomer)customer).clientDiscount : 10}">
            <div class="invalid-feedback">Please provide a valid discount (0-50).</div>
        </div>
        
        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
            <a href="${contextPath}/customer/list" class="btn btn-secondary me-md-2">
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
    
    // Toggle VIP discount field
    document.getElementById('isVip').addEventListener('change', function() {
        const discountContainer = document.getElementById('discountContainer');
        if (this.checked) {
            discountContainer.style.display = 'block';
            document.getElementById('clientDiscount').setAttribute('required', '');
        } else {
            discountContainer.style.display = 'none';
            document.getElementById('clientDiscount').removeAttribute('required');
        }
    });
})();
</script>

<%@include file="/templates/footer.jsp"%>