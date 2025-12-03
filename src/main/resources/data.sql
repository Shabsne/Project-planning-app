-- ------------------------------------------------------
-- ROLE
-- ------------------------------------------------------
INSERT INTO Role (roleName) VALUES
('Employee'),
('ProjectLeader'),
('Admin');

-- ------------------------------------------------------
-- EMPLOYEE
-- ------------------------------------------------------
INSERT INTO Employee (employeeRoleId, name, email, password) VALUES
(1, 'Alice Smith', 'alice@example.com', 'pass123'),
(2, 'Bob Johnson', 'bob@example.com', 'pass456'),
(3, 'Charlie Brown', 'charlie@example.com', 'pass789');

-- ------------------------------------------------------
-- PROJECT
-- ------------------------------------------------------
INSERT INTO Project (projectLeaderId, parentProjectId, name, description, startDate, endDate) VALUES
(2, NULL, 'Website Redesign', 'Redesign af firmaets website', '2025-12-01', '2025-12-31'),
(2, NULL, 'Mobile App', 'Udvikling af ny mobilapp', '2025-11-01', '2026-01-31');

-- ------------------------------------------------------
-- TASK
-- ------------------------------------------------------
INSERT INTO Task (projectId, parentTaskId, title, description, status, expectedHours, actualHours, deadline) VALUES
(1, NULL, 'Design Mockups', 'Lav designmockups for landing page', 'Open', 10, 0, '2025-12-05 17:00:00'),
(1, NULL, 'Implement Frontend', 'Frontend-udvikling af nye sider', 'Open', 40, 0, '2025-12-20 17:00:00'),
(2, NULL, 'Backend API', 'Udvikling af backend API', 'Open', 50, 0, '2026-01-15 17:00:00');

-- ------------------------------------------------------
-- PROJECT ↔ EMPLOYEE (MANY-TO-MANY)
-- ------------------------------------------------------
INSERT INTO ProjectEmployee (projectId, employeeId) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 3);

-- ------------------------------------------------------
--
