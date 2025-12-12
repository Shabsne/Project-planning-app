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


DROP TABLE IF EXISTS Project;

CREATE TABLE Project (
                         projectId INT PRIMARY KEY AUTO_INCREMENT,
                         projectLeaderId INT,
                         parentProjectId INT NULL,
                         name VARCHAR(150) NOT NULL,
                         description VARCHAR(2000),
                         startDate DATE,
                         endDate TIMESTAMP
);

-- Indsæt nogle testprojekter
INSERT INTO Project (projectLeaderId, parentProjectId, name, description, startDate, endDate)
VALUES
    (1, NULL, 'Website Redesign', 'Redesign af virksomhedens website', '2025-01-15', '2025-06-30 17:00:00'),
    (2, NULL, 'Mobil App', 'Udvikling af mobil app til kunder', '2025-02-01', '2025-08-31 18:00:00'),
    (3, 1, 'Frontend Modul', 'Frontend udvikling til website', '2025-01-20', '2025-04-30 16:00:00'),
    (1, 2, 'API Integration', 'Integrer API mellem systemer', '2025-03-01', '2025-07-15 17:30:00'),
    (2, NULL, 'Marketing Kampagne', 'Planlægning og udførelse af kampagne', '2025-04-01', '2025-09-01 12:00:00');