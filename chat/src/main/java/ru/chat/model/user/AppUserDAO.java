package ru.chat.model.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AppUserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean insert (String name, String email, String site, String password, String token) {
        String sql = "INSERT INTO users (site, email, active, username, token, validate, password) VALUES (?,?,?,?,?,?,?);";

        int insert = jdbcTemplate.update(sql, site, email, 1, name, token, 0, password);

        return insert > 0;
    }

    public boolean updateActive (int id, byte active) {
        String sql = "update users set active=? where id=?";

        int insert = jdbcTemplate.update(sql, active, id);

        return insert > 0;
    }

    public boolean update (int id, String what, String sql) {
        int insert = jdbcTemplate.update(sql, what, id);

        return insert > 0;
    }

    public AppUser findRepeats (String email, String url) {
        String sql = "SELECT * FROM users WHERE email=? OR site=? LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[] { email, url }, new AppUserMapper());
        } catch (Exception e) {
            return null;
        }
    }

    public int processing (String token, String email) {
        String sql = "SELECT id FROM users WHERE token=? AND email=? AND validate=0 LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[] { token, email }, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    public AppUser getById (int id) {
        String sql = "SELECT * FROM users WHERE id=? LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[] { id }, new AppUserMapper());
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updateValidate (int id) {
        String sql = "UPDATE users SET validate=1 WHERE id=?";

        try {
            int res = jdbcTemplate.update(sql, id);
            return res > 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    public AppUser findUserAccount (String email) {
        String sql = "SELECT * FROM users WHERE email=? AND validate=1 LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[] { email }, new AppUserMapper());
        } catch (Exception e) {
            return null;
        }
    }
}