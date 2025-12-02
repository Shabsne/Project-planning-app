package org.example.projectplanningapp.repositories.rowMappers;

import org.example.projectplanningapp.models.Employee;
import org.example.projectplanningapp.models.Project;


import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

public class ProjectRowMapper implements RowMapper<Project> {

    @Override
    public Project mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        Project project = new Project();

        // Generelle beskrivelser
        project.setId(rs.getInt("projectId"));
        project.setName(rs.getString("name"));
        project.setDescription(rs.getString("description"));


        // Start Date
        LocalDate startDate = rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null;
        project.setStartDate(startDate);

        //End Date
        Timestamp end = rs.getTimestamp("endDate");
        project.setEndDate(end != null ? end.toLocalDateTime() : null);


        // ParentProject
        int parentId = rs.getInt("parentProjectId");
        if (parentId != 0) {
            Project parent = new Project();
            parent.setId(parentId);

            // Kun ID bliver mappet her:
            project.setParentProject(parent);
        }


        //ProjectLeader
        int leaderId = rs.getInt("projectLeaderId");
        if (leaderId != 0) {
            Employee leader = new Employee();
            leader.setEmployeeId(leaderId);
            project.setProjectLeader(leader);
        }

        return project;
    }

}




