-- ============================================
-- H2 INITIALIZATION SCRIPT FOR EMPLOYEE TESTS
-- ============================================

-- Ryd op først
DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS Role;

-- Opret Role tabel (simplificeret version)
CREATE TABLE Role (
                      roleId INT PRIMARY KEY,
                      roleName VARCHAR(100) NOT NULL
);

-- Opret Employee tabel (præcis som dit skema)
CREATE TABLE Employee (
                          employeeId INT PRIMARY KEY AUTO_INCREMENT,
                          employeeRoleId INT,
                          name VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          password VARCHAR(100) NOT NULL
);

-- Indsæt roller
INSERT INTO Role (roleId, roleName) VALUES
                                        (1, 'Admin'),
                                        (2, 'Udvikler');

-- Indsæt test medarbejdere (originalt data fra din database)
INSERT INTO Employee (employeeRoleId, name, email, password) VALUES
                                                                 (1, 'Alice Hansen', 'alice@example.com', 'password123'),
                                                                 (2, 'Benjamin Larsen', 'benjamin@example.com', 'devpass'),
                                                                 (2, 'Clara Madsen', 'clara@example.com', 'devpass'),
                                                                 (1, 'Daniel Sørensen', 'daniel@example.com', 'designpass'),
                                                                 (2, 'Emma Kristensen', 'emma@example.com', 'test123'),
                                                                 (2, 'Frederik Nielsen', 'frederik@example.com', 'scrum123');

-- Indsæt edge cases for test
INSERT INTO Employee (employeeRoleId, name, email, password) VALUES
                                                                 (NULL, 'Test User Uden Rolle', 'test.norole@example.com', 'password123'),
                                                                 (2, 'Test Langt Navn', 'longname@example.com', 'pass123'),
                                                                 (2, 'Ægir Ørnulf Åse', 'special.chars@example.com', 'pass123');