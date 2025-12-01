-- ------------------------------------------------------
-- INSERT ROLES
-- ------------------------------------------------------
INSERT INTO Role (roleName) VALUES ('Admin');
INSERT INTO Role (roleName) VALUES ('Project Manager');
INSERT INTO Role (roleName) VALUES ('Developer');
INSERT INTO Role (roleName) VALUES ('Tester');

-- ------------------------------------------------------
-- INSERT EMPLOYEES
-- ------------------------------------------------------
INSERT INTO Employee (employeeRoleId, name, email, password) VALUES
                                                                 (1, 'Alice Johnson', 'alice@example.com', 'password1'),
                                                                 (2, 'Bob Smith', 'bob@example.com', 'password2'),
                                                                 (3, 'Charlie Brown', 'charlie@example.com', 'password3'),
                                                                 (4, 'Diana Prince', 'diana@example.com', 'password4');

-- ------------------------------------------------------
-- INSERT PROJECTS
-- ------------------------------------------------------
INSERT INTO Project (projectLeaderId, parentProjectId, name, description, startDate, endDate) VALUES
                                                                                                  (2, NULL, 'Project Alpha', 'First main project', '2025-01-01', '2025-06-30'),
                                                                                                  (2, 1, 'Project Alpha Phase 1', 'Phase 1 of Project Alpha', '2025-01-01', '2025-03-31'),
                                                                                                  (2, 1, 'Project Alpha Phase 2', 'Phase 2 of Project Alpha', '2025-04-01', '2025-06-30');

-- ------------------------------------------------------
-- INSERT TASKS
-- ------------------------------------------------------
INSERT INTO Task (projectId, parentTaskId, title, description, status, expectedHours, actualHours, deadline) VALUES
                                                                                                                 (2, NULL, 'Setup Environment', 'Set up dev environment', 'OPEN', 10, 0, '2025-01-05 17:00:00'),
                                                                                                                 (2, 1, 'Install Dependencies', 'Install all required libraries', 'OPEN', 5, 0, '2025-01-03 12:00:00'),
                                                                                                                 (3, NULL, 'Develop Feature A', 'Implement feature A', 'OPEN', 20, 0, '2025-04-15 17:00:00');

-- ------------------------------------------------------
-- ASSIGN PROJECTS TO EMPLOYEES
-- ------------------------------------------------------
INSERT INTO ProjectEmployee (projectId, employeeId) VALUES
                                                        (1, 2),
                                                        (1, 3),
                                                        (2, 3),
                                                        (3, 4);

-- ------------------------------------------------------
-- ASSIGN TASKS TO EMPLOYEES
-- ------------------------------------------------------
INSERT INTO TaskEmployee (taskId, employeeId) VALUES
                                                  (1, 3),
                                                  (2, 3),
                                                  (3, 4);
