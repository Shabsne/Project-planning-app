DROP DATABASE IF EXISTS projectplanning;
CREATE DATABASE projectplanning;
USE projectplanning;

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

INSERT INTO Role (roleName) VALUES
                                ('Project Leader'),
                                ('Developer'),
                                ('Designer'),
                                ('Tester'),
                                ('Scrum Master');

INSERT INTO Employee (employeeRoleId, name, email, password) VALUES
                                                                 (1, 'Alice Hansen', 'alice@example.com', 'password123'),
                                                                 (2, 'Benjamin Larsen', 'benjamin@example.com', 'devpass'),
                                                                 (2, 'Clara Madsen', 'clara@example.com', 'devpass'),
                                                                 (3, 'Daniel Sørensen', 'daniel@example.com', 'designpass'),
                                                                 (4, 'Emma Kristensen', 'emma@example.com', 'test123'),
                                                                 (5, 'Frederik Nielsen', 'frederik@example.com', 'scrum123');

INSERT INTO Project (projectLeaderId, parentProjectId, name, description, startDate, endDate) VALUES
                                                                                                  (1, NULL, 'Website Redesign', 'Redesign of the corporate website.', '2025-01-10', '2025-06-30'),
                                                                                                  (1, 1, 'Website Redesign – Phase 2', 'Second phase focusing on UX improvements.', '2025-03-01', '2025-09-30'),
                                                                                                  (5, NULL, 'Mobile App Development', 'Development of the new company mobile app.', '2025-02-01', '2025-12-01');


INSERT INTO Task (projectId, parentTaskId, title, description, status, estimatedHours, actualHours, deadline) VALUES
-- Tasks for Project 1 (Website Redesign)
(1, NULL, 'Create Wireframes', 'Initial wireframe creation.', 'TODO', 40, 10, '2025-02-15 16:00:00'),
(1, 1, 'Header Wireframe', 'Design header layout.', 'TODO', 10, NULL, '2025-02-05 16:00:00'),
(1, NULL, 'Frontend Development', 'Build the UI components.', 'TODO', 120, NULL, '2025-04-01 16:00:00'),

-- Tasks for Project 2 (Phase 2)
(2, NULL, 'User Testing', 'Conduct usability testing.', 'TODO', 30, NULL, '2025-05-01 16:00:00'),

-- Tasks for Project 3 (Mobile App)
(3, NULL, 'API Development', 'Develop backend API.', 'ONGOING', 150, 20, '2025-06-01 16:00:00'),
(3, NULL, 'App UI Mockups', 'Design UI mockups for mobile app.', 'TODO', 50, NULL, '2025-03-15 16:00:00');


INSERT INTO ProjectEmployee (projectId, employeeId) VALUES
-- Project 1
(1, 1), -- Alice (leader)
(1, 2), -- Benjamin
(1, 3), -- Clara
(1, 4), -- Daniel
(1, 5), -- Emma

-- Project 2
(2, 1), -- Alice
(2, 4), -- Daniel

-- Project 3
(3, 5), -- Frederik (Scrum Master)
(3, 2), -- Benjamin
(3, 3); -- Clara


INSERT INTO TaskEmployee (taskId, employeeId) VALUES
-- Project 1 tasksProject
(1, 4),  -- Daniel (Create Wireframes)
(2, 4),  -- Daniel (Header Wireframe)
(3, 2),  -- Benjamin (Frontend)
(3, 3),  -- Clara (Frontend)

-- Project 2 tasks
(4, 5),  -- Emma (Testing)

-- Project 3 tasks
(5, 2),  -- Benjamin (API)
(5, 3),  -- Clara (API)
(6, 4);  -- Daniel (UI Mockups)