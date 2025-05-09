<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="entites.customers_package.Customer"%>
<%@page import="entites.customers_package.VIPCustomer"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="/templates/header.jsp" %>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">
            <i class="bi bi-people me-2"></i>Customer Management
        </h2>
        <a href="${contextPath}/customer/new" class="btn btn-primary">
            <i class="bi bi-plus-circle me-1"></i>Add New Customer
        </a>
    </div>
    
    <c:if test="${not empty success}">
        <div class="alert alert-success auto-dismiss fade show" role="alert">
            <i class="bi bi-check-circle me-2"></i>${success}
        </div>
    </c:if>
    
    <c:if test="${not empty error}">
        <div class="alert alert-danger auto-dismiss fade show" role="alert">
            <i class="bi bi-exclamation-triangle me-2"></i>${error}
        </div>
    </c:if>
    
    <div class="card shadow-sm">
        <div class="card-body">
            <c:choose>
                <c:when test="${empty customers}">
                    <div class="alert alert-info">
                        <i class="bi bi-info-circle me-2"></i>No customers found
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Phone</th>
                                    <th>Account</th>
                                    <th>Type</th>
                                    <th>Discount</th>
                                    <th class="text-end">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${customers}" var="customer">
                                    <tr>
                                        <td>${customer.id}</td>
                                        <td>${customer.name}</td>
                                        <td>${customer.phone}</td>
                                        <td>${customer.account.username}</td>
                                        <td>
                                            <span class="badge ${customer instanceof VIPCustomer ? 'bg-warning' : 'bg-secondary'}">
                                                ${customer.getRole()}
                                            </span>
                                        </td>
                                        <td>
                                            <c:if test="${customer instanceof VIPCustomer}">
                                                $customer.clientDiscount}%
                                            </c:if>
                                        </td>
                                        <td class="text-end">
                                            <div class="btn-group btn-group-sm" role="group">
                                                <a href="${contextPath}/customer/edit?id=${customer.id}" 
                                                   class="btn btn-outline-primary"
                                                   data-bs-toggle="tooltip" title="Edit">
                                                    <i class="bi bi-pencil"></i>
                                                </a>
                                                <a href="${contextPath}/customer/delete?id=${customer.id}" 
                                                   class="btn btn-outline-danger confirm-before-action"
                                                   data-confirm-message="Are you sure you want to delete this customer?"
                                                   data-bs-toggle="tooltip" title="Delete">
                                                    <i class="bi bi-trash"></i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@include file="/templates/footer.jsp"%>