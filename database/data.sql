BEGIN TRANSACTION;

-- ================== Create tables with constraints ====================
DROP TABLE IF EXISTS Customer CASCADE;
DROP TABLE IF EXISTS Trailer CASCADE;
DROP TABLE IF EXISTS Shipper CASCADE;
DROP TABLE IF EXISTS Unloader CASCADE;

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
    CONSTRAINT FK_Customer_Trailer FOREIGN KEY (trailer_id) REFERENCES Trailer(trailer_id),
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


-- ================== Insert initial data ====================

-- Insert Shippers
INSERT INTO Shipper (name, address, city, state, zip_code) VALUES
('Swift Logistics', '500 Shipping Lane', 'Chicago', 'IL', '60601'),
('Global Freight Solutions', '1200 Cargo Drive', 'Dallas', 'TX', '75201'),
('Elite Shipping Co.', '800 Transport Blvd', 'Los Angeles', 'CA', '90012');

-- Insert Trailers
INSERT INTO Trailer (trailer_number, trailer_type, shipper_id) VALUES
('OF1234', 'Local/City', 1),
('LE4567', 'Lift-Gate', 2),
('B2389', 'Box Truck', 3);

-- Insert Customers
INSERT INTO Customer (order_number, name, address, city, state, zip_code, door_number, trailer_id, handling_unit, weight) VALUES
-- OF1234 Trailer Customers
('123456789', 'John Doe', '123 Maple Street', 'Boston', 'MA', '02108', 'Door 12', 1, 10, 15000),
('234567891', 'Jane Smith', '456 Oak Avenue', 'Chicago', 'IL', '60601', 'Door 45', 1, 7, 89),
('345678911', 'Alice Johnson', '789 Pine Road', 'Houston', 'TX', '77002', 'Door 7', 1, 2, 61), -- Changed from 345678912
('456789123', 'Bob Brown', '101 Elm Boulevard', 'Phoenix', 'AZ', '85004', 'Door 33', 1, 4, 113),
('567891234', 'Carol White', '202 Cedar Lane', 'Philadelphia', 'PA', '19107', 'Door 19', 1, 6, 99),
('678912345', 'Dave Wilson', '303 Birch Drive', 'San Antonio', 'TX', '78205', 'Door 28', 1, 5, 65),
('789123456', 'Eve Davis', '404 Walnut Way', 'San Diego', 'CA', '92101', 'Door 50', 1, 8, 71),
('891234567', 'Frank Miller', '505 Ash Terrace', 'Dallas', 'TX', '75201', 'Door 14', 1, 3, 103),
('912345678', 'Grace Lee', '606 Redwood Place', 'San Jose', 'CA', '95113', 'Door 37', 1, 2, 58),
('123456780', 'Henry Clark', '707 Cypress Court', 'Austin', 'TX', '78701', 'Door 1', 1, 9, 75),
('234567801', 'Ivy Walker', '808 Willow Bend', 'Jacksonville', 'FL', '32202', 'Door 22', 1, 1, 89),
('345678902', 'Jack Harris', '909 Spruce Hill', 'Fort Worth', 'TX', '76102', 'Door 40', 1, 7, 91), -- Changed from 345678912
('456789103', 'Karen Lewis', '1010 Magnolia Circle', 'Columbus', 'OH', '43215', 'Door 5', 1, 3, 102),
('567891214', 'Larry King', '1111 Cherry Loop', 'Charlotte', 'NC', '28202', 'Door 48', 1, 2, 63),
('678912325', 'Mary Young', '1212 Poplar Crescent', 'San Francisco', 'CA', '94103', 'Door 26', 1, 4, 111),
('789123436', 'Nick Hall', '1313 Fir Avenue', 'Indianapolis', 'IN', '46204', 'Door 11', 1, 6, 93),
('891234547', 'Olivia Green', '1414 Maple Street', 'Seattle', 'WA', '98104', 'Door 30', 1, 5, 61),
('912345658', 'Peter Adams', '1515 Oak Avenue', 'Denver', 'CO', '80202', 'Door 49', 1, 3, 83),
('123456769', 'Quinn Baker', '1616 Pine Road', 'Washington', 'DC', '20001', 'Door 17', 1, 8, 116),
('234567890', 'Ruby Carter', '1717 Elm Boulevard', 'Boston', 'MA', '02116', 'Door 36', 1, 7, 80),
('345678901', 'Steve Parker', '1818 Cedar Lane', 'Nashville', 'TN', '37203', 'Door 8', 1, 9, 57),
('456789012', 'Tina Scott', '1919 Birch Drive', 'Baltimore', 'MD', '21202', 'Door 44', 1, 2, 66),
('567891123', 'Ursula Evans', '2020 Walnut Way', 'Oklahoma City', 'OK', '73102', 'Door 21', 1, 4, 109),
('678912234', 'Victor Price', '2121 Ash Terrace', 'Louisville', 'KY', '40202', 'Door 39', 1, 5, 98),
('789123345', 'Wendy Bell', '2222 Redwood Place', 'Portland', 'OR', '97204', 'Door 25', 1, 3, 82),
('891234456', 'Xavier Turner', '2323 Cypress Court', 'Las Vegas', 'NV', '89101', 'Door 16', 1, 6, 105),

-- LE4567 Trailer Customers
('123456677', 'Zachary Howard', '2525 Spruce Hill', 'Milwaukee', 'WI', '53202', 'Door 34', 2, 7, 117), -- Changed from 123456678
('234567788', 'Angela Bryant', '2626 Magnolia Circle', 'Albuquerque', 'NM', '87102', 'Door 27', 2, 5, 77), -- Changed from 234567789
('345678899', 'Brian Lewis', '2727 Cherry Loop', 'Tucson', 'AZ', '85701', 'Door 10', 2, 4, 102), -- Changed from 345678890
('456789900', 'Charlotte Hayes', '2828 Poplar Crescent', 'Fresno', 'CA', '93721', 'Door 41', 2, 6, 63), -- Changed from 456789901
('567891011', 'Derek Long', '2929 Fir Avenue', 'Sacramento', 'CA', '95814', 'Door 15', 2, 3, 114), -- Changed from 567891012
('678912122', 'Emily Murphy', '3030 Maple Street', 'Kansas City', 'MO', '64106', 'Door 38', 2, 2, 90), -- Changed from 678912123
('789123233', 'Fiona Sanders', '3131 Oak Avenue', 'Mesa', 'AZ', '85201', 'Door 2', 2, 8, 60), -- Changed from 789123234
('891234344', 'George Reed', '3232 Pine Road', 'Atlanta', 'GA', '30303', 'Door 29', 2, 7, 75), -- Changed from 891234345
('912345455', 'Hannah Collins', '3333 Elm Boulevard', 'Omaha', 'NE', '68102', 'Door 18', 2, 5, 107), -- Changed from 912345456
('123456566', 'Ian Powell', '3434 Cedar Lane', 'Colorado Springs', 'CO', '80903', 'Door 6', 2, 9, 85), -- Changed from 123456567
('234567677', 'Jenna Ross', '3535 Birch Drive', 'Raleigh', 'NC', '27601', 'Door 42', 2, 6, 94), -- Changed from 234567678
('345678788', 'Kevin Mitchell', '3636 Walnut Way', 'Miami', 'FL', '33128', 'Door 23', 2, 3, 58), -- Changed from 345678789
('456789899', 'Laura Perez', '3737 Ash Terrace', 'Long Beach', 'CA', '90802', 'Door 4', 2, 7, 106), -- Changed from 456789890
('567891000', 'Matt Bennett', '3838 Redwood Place', 'Virginia Beach', 'VA', '23451', 'Door 31', 2, 2, 75), -- Changed from 567891001
('678912111', 'Nora Ward', '3939 Cypress Court', 'Oakland', 'CA', '94607', 'Door 20', 2, 4, 111), -- Changed from 678912112
('789123222', 'Oscar Hughes', '4040 Willow Bend', 'Minneapolis', 'MN', '55402', 'Door 35', 2, 5, 90), -- Changed from 789123223
('891234333', 'Paula Rogers', '4141 Spruce Hill', 'Tulsa', 'OK', '74103', 'Door 13', 2, 3, 115), -- Changed from 891234334
('912345444', 'Quinn Murphy', '4242 Magnolia Circle', 'Arlington', 'TX', '76010', 'Door 46', 2, 8, 63), -- Changed from 912345445
('123456555', 'Riley Gray', '4343 Cherry Loop', 'New Orleans', 'LA', '70112', 'Door 9', 2, 9, 71), -- Changed from 123456556
('234567666', 'Sophie Cooper', '4444 Poplar Crescent', 'Wichita', 'KS', '67202', 'Door 24', 2, 6, 99), -- Changed from 234567667
('345678777', 'Tom Campbell', '4545 Fir Avenue', 'Cleveland', 'OH', '44113', 'Door 32', 2, 4, 109), -- Changed from 345678778
('456789888', 'Una Grant', '4646 Maple Street', 'Tampa', 'FL', '33602', 'Door 47', 2, 3, 84), -- Changed from 456789889
('567890999', 'Victor Johnson', '4747 Oak Avenue', 'Bakersfield', 'CA', '93301', 'Door 43', 2, 7, 60), -- Changed from 567891000
('678911111', 'Wendy Bell', '4848 Pine Road', 'Aurora', 'CO', '80010', 'Door 10', 2, 4, 102), -- Changed from 678912111
('789122222', 'Xander Turner', '4949 Cypress Court', 'Anaheim', 'CA', '92805', 'Door 23', 2, 3, 58), -- Changed from 789123221
('891233333', 'Yuri Foster', '5050 Walnut Way', 'Honolulu', 'HI', '96813', 'Door 3', 2, 8, 69), -- Changed from 891234332

-- B2389 Trailer Customers
('912345442', 'Zachary Howard', '5151 Maple Street', 'Henderson', 'NV', '89011', 'Door 34', 3, 7, 117), -- Changed from 912345443
('123456553', 'Abigail Clark', '5252 Oak Avenue', 'Stockton', 'CA', '95202', 'Door 27', 3, 5, 77), -- Changed from 123456554
('234567664', 'Blake Parker', '5353 Birch Drive', 'Lexington', 'KY', '40507', 'Door 10', 3, 4, 102), -- Changed from 234567665
('345678775', 'Chloe Collins', '5454 Redwood Place', 'Corpus Christi', 'TX', '78401', 'Door 41', 3, 6, 63), -- Changed from 345678776
('456789886', 'Dominic Lewis', '5555 Cherry Loop', 'Riverside', 'CA', '92501', 'Door 15', 3, 3, 114), -- Changed from 456789887
('567891997', 'Evelyn Brown', '5656 Spruce Hill', 'Santa Ana', 'CA', '92701', 'Door 38', 3, 2, 90), -- Changed from 567891998
('678912108', 'Fred Thompson', '5757 Willow Bend', 'Orlando', 'FL', '32801', 'Door 2', 3, 8, 60), -- Changed from 678912109
('789123219', 'Gina Carter', '5858 Elm Boulevard', 'Irvine', 'CA', '92612', 'Door 29', 3, 7, 75), -- Changed from 789123220
('891234330', 'Henry Powell', '5959 Cedar Lane', 'Cincinnati', 'OH', '45202', 'Door 18', 3, 5, 107), -- Changed from 891234331
('912345441', 'Isabella Ross', '6060 Fir Avenue', 'Pittsburgh', 'PA', '15222', 'Door 6', 3, 9, 85), -- Changed from 912345442
('123456552', 'Jason Ward', '6161 Maple Street', 'St. Louis', 'MO', '63101', 'Door 42', 3, 6, 94), -- Changed from 123456553
('234567663', 'Kyle Foster', '6262 Pine Road', 'Greensboro', 'NC', '27401', 'Door 23', 3, 3, 58), -- Changed from 234567664
('345678774', 'Lily Hughes', '6363 Oak Avenue', 'Newark', 'NJ', '07102', 'Door 4', 3, 7, 106), -- Changed from 345678775
('456789885', 'Michael Scott', '6464 Birch Drive', 'Buffalo', 'NY', '14203', 'Door 31', 3, 2, 75), -- Changed from 456789886
('567891996', 'Nancy Price', '6565 Willow Bend', 'Plano', 'TX', '75074', 'Door 20', 3, 4, 111), -- Changed from 567891997
('678912107', 'Olivia Parker', '6666 Spruce Hill', 'Lincoln', 'NE', '68508', 'Door 35', 3, 5, 90), -- Changed from 678912108
('789123218', 'Paul Rogers', '6767 Elm Boulevard', 'Anchorage', 'AK', '99501', 'Door 13', 3, 3, 115), -- Changed from 789123219
('891234329', 'Quincy Lewis', '6868 Cedar Lane', 'Durham', 'NC', '27701', 'Door 46', 3, 8, 63), -- Changed from 891234330
('912345440', 'Rachel Gray', '6969 Poplar Crescent', 'Jersey City', 'NJ', '07302', 'Door 9', 3, 9, 71), -- Changed from 912345441
('123456551', 'Samuel Reed', '7070 Maple Street', 'Chandler', 'AZ', '85224', 'Door 24', 3, 6, 99), -- Changed from 123456552
('234567662', 'Taylor Bell', '7171 Oak Avenue', 'Lubbock', 'TX', '79401', 'Door 32', 3, 4, 109), -- Changed from 234567663
('345678773', 'Uma Green', '7272 Birch Drive', 'Madison', 'WI', '53703', 'Door 47', 3, 3, 84), -- Changed from 345678774
('456789884', 'Vincent King', '7373 Cedar Lane', 'Reno', 'NV', '89501', 'Door 43', 3, 7, 60), -- Changed from 456789885
('567891995', 'Willow Adams', '7474 Pine Road', 'Fort Wayne', 'IN', '46802', 'Door 10', 3, 4, 102), -- Changed from 567891996
('678912106', 'Xavier Turner', '7575 Cypress Court', 'Baton Rouge', 'LA', '70801', 'Door 23', 3, 3, 58); -- Changed from 678912107

--Unloader insert
INSERT INTO Unloader (name, shift, employee_number) VALUES
('John Smith', '2nd Shift', '45782'),
('Emily Johnson', '2nd Shift', '39821'),
('Michael Williams', '2nd Shift', '61245'),
('Sarah Brown', '2nd Shift', '53467'),
('David Jones', '2nd Shift', '78934'),
('Olivia Garcia', '2nd Shift', '24589'),
('James Martinez', '2nd Shift', '87653'),
('Emma Rodriguez', '2nd Shift', '32176'),
('Robert Hernandez', '2nd Shift', '65432'),
('Sophia Lopez', '2nd Shift', '98712'),
('William Gonzalez', '2nd Shift', '12345'),
('Ava Wilson', '2nd Shift', '56789'),
('Joseph Anderson', '2nd Shift', '43210'),
('Mia Thomas', '2nd Shift', '67895'),
('Charles Taylor', '2nd Shift', '34567'),
('Isabella Moore', '2nd Shift', '89012'),
('Daniel Jackson', '2nd Shift', '76543'),
('Amelia Martin', '2nd Shift', '21098'),
('Jane Davis', '2nd Shift', '87654'),
('Thomas Miller', '2nd Shift', '10987');

COMMIT;