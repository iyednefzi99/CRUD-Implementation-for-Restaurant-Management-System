-- Database creation
CREATE DATABASE restaurant_management;
USE restaurant_management;

-- Account table
CREATE TABLE accounts (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    status ENUM('ACTIVE', 'CLOSED', 'CANCELED', 'BLACKLISTED', 'SUSPENDED', 
                'PENDING', 'FROZEN', 'INACTIVE', 'ARCHIVED', 'DELETED',
                'VERIFIED', 'UNVERIFIED', 'LOCKED', 'REINSTATED',
                'EXPIRED', 'TERMINATED', 'UNDER_REVIEW', 'DORMANT', 'SUSPICIOUS') NOT NULL
);

-- Customer tables
CREATE TABLE customers (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    account_username VARCHAR(50) NOT NULL,
    last_visited DATETIME,
    client_discount INT DEFAULT 0,
    FOREIGN KEY (account_username) REFERENCES accounts(username)
);

-- Employee tables
CREATE TABLE employees (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    account_username VARCHAR(50) NOT NULL,
    date_joined DATE NOT NULL,
    role ENUM('WAITER', 'MANAGER', 'RECEPTIONIST') NOT NULL,
    FOREIGN KEY (account_username) REFERENCES accounts(username)
);

-- Menu tables
CREATE TABLE menus (
    menu_id VARCHAR(20) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE menu_sections (
    section_id VARCHAR(20) PRIMARY KEY,
    menu_id VARCHAR(20) NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    FOREIGN KEY (menu_id) REFERENCES menus(menu_id)
);

CREATE TABLE menu_items (
    item_id VARCHAR(20) PRIMARY KEY,
    section_id VARCHAR(20) NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (section_id) REFERENCES menu_sections(section_id)
);

-- Table management
CREATE TABLE tables (
    table_id VARCHAR(20) PRIMARY KEY,
    max_capacity INT NOT NULL,
    location_identifier INT NOT NULL,
    status ENUM('FREE', 'RESERVED', 'OCCUPIED', 'CANCELED', 
                'WAITLISTED', 'IN_SERVICE', 'OUT_OF_SERVICE',
                'PENDING', 'UNAVAILABLE', 'READY_FOR_CLEANING',
                'BEING_CLEANED', 'BOOKED', 'CHECKED_OUT',
                'TEMPORARILY_CLOSED', 'RESERVED_FOR_EVENT',
                'PARTIALLY_OCCUPIED') NOT NULL
);

-- Order management
CREATE TABLE orders (
    order_id VARCHAR(20) PRIMARY KEY,
    customer_id VARCHAR(20) NOT NULL,
    waiter_id VARCHAR(20) NOT NULL,
    table_id VARCHAR(20) NOT NULL,
    status ENUM('NEW', 'IN_PROGRESS', 'READY_FOR_SERVING', 'DELIVERED',
                'CANCELLED', 'PAYMENT_PENDING', 'PAYMENT_FAILED',
                'COMPLETED', 'REFUNDED', 'ON_HOLD', 'DISPATCHED',
                'RETURN_REQUESTED', 'RETURNED') NOT NULL,
    creation_time DATETIME NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (waiter_id) REFERENCES employees(id),
    FOREIGN KEY (table_id) REFERENCES tables(table_id)
);

CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(20) NOT NULL,
    item_id VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    special_request TEXT,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (item_id) REFERENCES menu_items(item_id)
);

-- Reservation management
CREATE TABLE reservations (
    reservation_id VARCHAR(20) PRIMARY KEY,
    customer_id VARCHAR(20) NOT NULL,
    employee_id VARCHAR(20) NOT NULL,
    reservation_time DATETIME NOT NULL,
    party_size INT NOT NULL,
    special_requests TEXT,
    status ENUM('PENDING', 'CONFIRMED', 'CANCELED', 'ABANDONED',
                'COMPLETED', 'NO_SHOW', 'MODIFIED', 'EXPIRED',
                'WAITLISTED', 'REJECTED', 'IN_PROGRESS', 'VERIFIED',
                'PARTIALLY_CONFIRMED', 'AWAITING_PAYMENT',
                'CANCELLED_BY_USER', 'CANCELLED_BY_SYSTEM', 'RESCHEDULED') NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);