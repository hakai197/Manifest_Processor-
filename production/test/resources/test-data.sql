-- Start a transaction
BEGIN TRANSACTION;

-- ==================   Create tables, columns, and constraints (DDL)  ====================

-- DROP TABLE IF EXISTS statements
DROP TABLE IF EXISTS Customer CASCADE;
DROP TABLE IF EXISTS Trailer CASCADE;
DROP TABLE IF EXISTS Shipper CASCADE;
DROP TABLE IF EXISTS Unloader CASCADE;

-- CREATE TABLE statements
CREATE TABLE Shipper (
    shipper_id SERIAL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    CONSTRAINT PK_Shipper PRIMARY KEY (shipper_id)
);

CREATE TABLE Trailer (
    trailer_id SERIAL,
    trailer_number VARCHAR(20) NOT NULL,
    trailer_type VARCHAR(50) NOT NULL,
    shipper_id INTEGER NOT NULL,
    CONSTRAINT PK_Trailer PRIMARY KEY (trailer_id),
    CONSTRAINT FK_Trailer_Shipper FOREIGN KEY (shipper_id) REFERENCES Shipper(shipper_id) ON DELETE CASCADE,
    CONSTRAINT UQ_Trailer_number UNIQUE (trailer_number)
);

CREATE TABLE Customer (
    customer_id SERIAL,
    order_number VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    door_number VARCHAR(10) NOT NULL,
    trailer_id INTEGER NOT NULL,
    handling_unit INTEGER NOT NULL,
    weight INTEGER NOT NULL,
    CONSTRAINT PK_Customer PRIMARY KEY (customer_id),
    CONSTRAINT FK_Customer_Trailer FOREIGN KEY (trailer_id) REFERENCES Trailer(trailer_id) ON DELETE CASCADE,
    CONSTRAINT UQ_Order_number UNIQUE (order_number)
);

CREATE TABLE Unloader (
    employee_id SERIAL,
    name VARCHAR(100) NOT NULL,
    shift VARCHAR(100) NOT NULL,
    employee_number VARCHAR(100) NOT NULL,
    CONSTRAINT PK_Unloader PRIMARY KEY (employee_id),
    CONSTRAINT UQ_Employee_number UNIQUE (employee_number)
);

-- ==================   Insert test data (DML)  ====================

-- INSERT statements
INSERT INTO Shipper (name, address, city, state, zip_code) VALUES
('R&L Carriers', '600 Gilliam Road', 'Wilmington', 'OH', '43178');


INSERT INTO Trailer (trailer_number, trailer_type, shipper_id) VALUES
('OF4328', 'Local/City', 1);


INSERT INTO Customer (order_number, name, address, city, state, zip_code, door_number, trailer_id, handling_unit, weight) VALUES
('4887500387', 'Alec Holland', '1972 Wein Way', 'Baton Rouge', 'LA', '87767', 'Door 12', 1, 10, 15000), --
('087045987', 'Eobard Thawne', '3500 Flash Road', 'Central City', 'KA', '53202', 'Door 34', 1, 7, 117),
('398847905', 'Buddy Barker', '39 Animals Street', 'Star City', 'NV', '89011', 'Door 34', 1, 7, 117);

INSERT INTO Unloader (name, shift, employee_number) VALUES
('Erik Lehnsherr', '2nd Shift', '182734');


COMMIT;