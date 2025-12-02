DROP TABLE IF EXISTS Role
DROP TABLE IF EXISTS Employee
DROP TABLE IF EXISTS Project
DROP TABLE IF EXISTS Task
DROP TABLE IF EXISTS ProjectEmployee
DROP TABLE IF EXISTS TaskEmployee
-- ------------------------------------------------------
-- INSERT ROLES
-- ------------------------------------------------------
CREATE TABLE IF NOT EXISTS Role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    roleName VARCHAR(50) NOT NULL
);
-- ------------------------------------------------------
-- INSERT EMPLOYEES
-- ------------------------------------------------------
CREATE TABLE IF NOT EXISTS Employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employeeRoleId INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL,
    password VARCHAR(200) NOT NULL,
    FOREIGN KEY (employeeRoleId) REFERENCES Role(id)
    );
-- ------------------------------------------------------
-- INSERT PROJECTS
-- ------------------------------------------------------
CREATE TABLE IF NOT EXISTS Project (
    id INT AUTO_INCREMENT PRIMARY KEY,
    projectLeaderId INT NOT NULL,
    parentProjectId INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    startDate DATE,
    endDate DATE,
    FOREIGN KEY (projectLeaderId) REFERENCES Employee(id),
    FOREIGN KEY (parentProjectId) REFERENCES Project(id)
);
-- ------------------------------------------------------
-- INSERT TASKS
-- ------------------------------------------------------
CREATE TABLE IF NOT EXISTS Task (
    id INT AUTO_INCREMENT PRIMARY KEY,
    projectId INT NOT NULL,
    parentTaskId INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    expectedHours INT NOT NULL,
    actualHours INT NOT NULL,
    deadline DATETIME,
    FOREIGN KEY (projectId) REFERENCES Project(id)
    FOREIGN KEY (parentTaskId) REFERENCES Task(id)
);
-- ------------------------------------------------------
-- ASSIGN PROJECTS TO EMPLOYEES
-- ------------------------------------------------------
CREATE TABLE ProjectEmployee (
    projectId INT NOT NULL,
    employeeId INT NOT NULL,
    PRIMARY KEY(projectId, employeeId),
    FOREIGN KEY(projectId) REFERENCES Project(id),
    FOREIGN KEY(employeeId) REFERENCES Employee(id)
);
-- ------------------------------------------------------
-- ASSIGN TASKS TO EMPLOYEES
-- ------------------------------------------------------
CREATE TABLE TaskEmployee (
    taskId INT NOT NULL,
    employeeId INT NOT NULL,
    PRIMARY KEY (taskId, employeeId),
    FOREIGN KEY (taskId) REFERENCES Task(id),
    FOREIGN KEY (employeeId) REFERENCES Employee(id)
);
