package ru.chat.model.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class AppUserMapper implements RowMapper<AppUser> {

    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new AppUser( rs.getInt("id"),
                rs.getString("site"),
                rs.getString("email"),
                rs.getString("createdAt"),
                rs.getByte("active"),
                rs.getString("username"),
                rs.getString("token"),
                rs.getByte("validate"),
                rs.getString("password"),
                rs.getString("last_login"));
    }
}
