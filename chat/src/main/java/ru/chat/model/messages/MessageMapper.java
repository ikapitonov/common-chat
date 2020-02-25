package ru.chat.model.messages;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class MessageMapper implements RowMapper<MessageItem> {

    @Override
    public MessageItem mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new MessageItem(
                rs.getLong("mes_id"),
                rs.getString("username"),
                rs.getByte("admin"),
                rs.getString("message"),
                rs.getString("DATE_FORMAT(`date`, '%H:%i')"),
                rs.getInt("parent_id")
        );
    }
}