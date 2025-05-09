<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Restaurant Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    
    <!-- Custom CSS -->
    <link rel="stylesheet" href="<%= contextPath %>/assets/css/style.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="<%= contextPath %>/index.jsp">
                <i class="bi bi-cup-hot-fill"></i> Restaurant MS
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="<%= contextPath %>/index.jsp">
                            <i class="bi bi-house-door"></i> Home
                        </a>
                    </li>
                    
                    <!-- Accounts Dropdown -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="accountsDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="bi bi-people"></i> Accounts
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/account/list">
    <i class="bi bi-list-ul"></i> List Accounts
</a></li>
                            <li><a class="dropdown-item" href="<%= contextPath %>/account/form">
                                <i class="bi bi-plus-circle"></i> Create Account</a></li>
                        </ul>
                    </li>
                    
                    <!-- Customers Dropdown -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="customersDropdown" role="button" data-bs-toggle="dropdown">
                            <i class="bi bi-person-badge"></i> Customers
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="<%= contextPath %>/customer/list">
                                <i class="bi bi-list-ul"></i> List Customers</a></li>
                            <li><a class="dropdown-item" href="<%= contextPath %>/customer/form">
                                <i class="bi bi-plus-circle"></i> Create Customer</a></li>
                        </ul>
                    </li>
               <li class="nav-item dropdown">
    <a class="nav-link dropdown-toggle" href="#" id="ordersDropdown" role="button" data-bs-toggle="dropdown">
        <i class="bi bi-cart"></i> Orders
    </a>
    <ul class="dropdown-menu">
        <li><a class="dropdown-item" href="<%= contextPath %>/order/list">
            <i class="bi bi-list-ul"></i> List Orders</a></li>
        <li><a class="dropdown-item" href="<%= contextPath %>/order/form">
            <i class="bi bi-plus-circle"></i> Create Order</a></li>
    </ul>
</li>
                <li class="dropdown">
                    <a href="#">Reservations</a>
                    <div class="dropdown-content">
                        <a href="<%= contextPath %>/reservation/list">List Reservations</a>
                        <a href="<%= contextPath %>/reservation/form">Create Reservation</a>
                    </div>
                </li>
                <li class="dropdown">
                    <a href="#">Reviews</a>
                    <div class="dropdown-content">
                        <a href="<%= contextPath %>/review/list">List Reviews</a>
                        <a href="<%= contextPath %>/review/form">Create Review</a>
                    </div>
                </li>
                <li class="dropdown">
                    <a href="#">Tables</a>
                    <div class="dropdown-content">
                        <a href="<%= contextPath %>/table/list">List Tables</a>
                        <a href="<%= contextPath %>/table/form">Create Table</a>
                    </div>
                </li>
           </ul>
                <div class="d-flex">
                    <a href="#" class="btn btn-outline-light me-2">
                        <i class="bi bi-box-arrow-in-right"></i> Login
                    </a>
                </div>
            </div>
        </div>
    </nav>

