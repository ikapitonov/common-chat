package ru.chat.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Site {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getId (String site) {
        String sql = "SELECT id FROM users WHERE site=? AND active=1 LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{ site }, Integer.class);
        }
        catch (Exception e) {
            return 0;
        }
    }

    public String validateById (int id) {
        String sql = "SELECT token FROM users WHERE id=? AND active=1 LIMIT 1";

        try {
            String result = jdbcTemplate.queryForObject(sql, new Object[]{ id }, String.class);
            return result.isEmpty() ? null : result;
        }
        catch (Exception e) {
            return null;
        }
    }
}
