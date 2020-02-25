package ru.chat.model.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;

@Repository
public class Messages {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Messages () {

    }

    public ArrayList<MessageItem> selectItems (int parent_id, int offset) {
        String sql = "SELECT mes_id,parent_id,admin,message,username,DATE_FORMAT(`date`, '%H:%i') FROM messages WHERE parent_id=? ORDER BY mes_id DESC LIMIT ?, 50";

        try {
            ArrayList<MessageItem> items = (ArrayList) jdbcTemplate.query(sql, new Object[]{parent_id, offset}, new MessageMapper());
            Collections.reverse(items);
            return items;
        }
        catch (Exception e) {
            return null;
        }
    }

    public boolean insert (int parent_id, String message, String username, byte admin) {
        int update = jdbcTemplate.update("INSERT INTO messages(parent_id,admin,message,username) VALUES(?,?,?,?)", parent_id, admin, message, username);

        return update > 0;
    }
}
