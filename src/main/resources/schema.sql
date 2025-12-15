DROP DATABASE IF EXISTS projectplanning;
CREATE DATABASE projectplanning;
USE projectplanning;
c
-- ------------------------------------------------------
-- ROLE
-- ------------------------------------------------------
CREATE TABLE Role (
roleId INT PRIMARY KEY AUTO_INCREMENT,
roleName VARCHAR(100) NOT NULL
);

-- ------------------------------------------------------
-- EMPLOYEE (tidl. Profile)
-- ------------------------------------------------------
CREATE TABLE Employee (
employeeId INT PRIMARY KEY AUTO_INCREMENT,
employeeRoleId INT,
name VARCHAR(100) NOT NULL,
email VARCHAR(150) NOT NULL UNIQUE,
password VARCHAR(100) NOT NULL,

CONSTRAINT fk_employee_role
FOREIGN KEY (employeeRoleId) REFERENCES Role(roleId)
ON DELETE SET NULL
ON UPDATE CASCADE
);
-- ------------------------------------------------------
-- PROJECT
-- ------------------------------------------------------
CREATE TABLE Project (
projectId INT PRIMARY KEY AUTO_INCREMENT,

projectLeaderId INT,
parentProjectId INT NULL,

name VARCHAR(150) NOT NULL,
description VARCHAR(2000),
startDate DATE,
endDate DATETIME,

CONSTRAINT fk_project_leader
    FOREIGN KEY (projectLeaderId) REFERENCES Employee(employeeId)
        ON DELETE SET NULL
        ON UPDATE CASCADE,

CONSTRAINT fk_project_parent
    FOREIGN KEY (parentProjectId) REFERENCES Project(projectId)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- ------------------------------------------------------
-- TASK
-- ------------------------------------------------------
CREATE TABLE Task (
taskId INT PRIMARY KEY AUTO_INCREMENT,

projectId INT,
parentTaskId INT NULL,

title VARCHAR(50) NOT NULL,
description VARCHAR(2000),
status VARCHAR(50),
estimatedHours INT,
actualHours INT,
deadline DATETIME,

CONSTRAINT fk_task_project
    FOREIGN KEY (projectId) REFERENCES Project(projectId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

CONSTRAINT fk_task_parent
    FOREIGN KEY (parentTaskId) REFERENCES Task(taskId)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- ------------------------------------------------------
-- MANY-TO-MANY : PROJECT ↔ EMPLOYEE
-- ------------------------------------------------------
CREATE TABLE ProjectEmployee (
projectId INT,
employeeId INT,

PRIMARY KEY (projectId, employeeId),

CONSTRAINT fk_pe_project
    FOREIGN KEY (projectId) REFERENCES Project(projectId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

CONSTRAINT fk_pe_employee
    FOREIGN KEY (employeeId) REFERENCES Employee(employeeId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ------------------------------------------------------
-- MANY-TO-MANY : TASK ↔ EMPLOYEE
-- ------------------------------------------------------
CREATE TABLE TaskEmployee (
taskId INT,
employeeId INT,

PRIMARY KEY (taskId, employeeId),

CONSTRAINT fk_te_task
    FOREIGN KEY (taskId) REFERENCES Task(taskId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

CONSTRAINT fk_te_employee
    FOREIGN KEY (employeeId) REFERENCES Employee(employeeId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);