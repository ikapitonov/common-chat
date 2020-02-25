package ru.chat.window;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SiteValidate {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean find (String site) {
        try {
            int total = jdbcTemplate.queryForObject("SELECT count(id) FROM sites WHERE site=" + site, new Object[]{ site }, Integer.class);
            return total > 0;
        }
        catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
