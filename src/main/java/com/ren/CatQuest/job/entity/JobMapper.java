package com.ren.CatQuest.job.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JobMapper {
    public static JobEntity fromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String title = rs.getString("title");
        Long companyId = rs.getLong("company_id");
        if (rs.wasNull()) companyId = null;
        Long locationId = rs.getLong("location_id");
        if (rs.wasNull()) locationId = null;
        String description = rs.getString("description");
        Integer salary = rs.getObject("salary") == null ? null : rs.getInt("salary");
        String typeStr = rs.getString("job_type");
        JobType type = typeStr == null ? null : JobType.valueOf(typeStr);
        String jobUrl = rs.getString("job_url");
        String source = rs.getString("source");
        java.sql.Timestamp ts = rs.getTimestamp("posted_at");

        return new JobEntity (id, title, companyId, locationId, description, salary, type, jobUrl, source);
    }
}