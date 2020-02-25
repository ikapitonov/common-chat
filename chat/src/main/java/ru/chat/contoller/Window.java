package ru.chat.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.chat.model.Site;
import ru.chat.model.messages.MessageItem;
import ru.chat.model.messages.Messages;
import ru.chat.websocket.utils.Counter;

import java.util.ArrayList;

@Controller
public class Window {

    @Autowired
    private Messages sqlMessage;

    @Autowired
    private Site sqlSite;

    @GetMapping("/window")
    public String welcomePage (@RequestParam(name="site") String site,
                               @RequestParam(name="username") String username,
                               @RequestParam(name="token", required=false, defaultValue="") String token,
                               Model model) {
        if (site.isEmpty() || username.isEmpty()) {
            return "no";
        }

        int siteId = sqlSite.getId(site);

        if (siteId == 0) {
            return "no";
        }

        ArrayList<MessageItem> items = sqlMessage.selectItems(siteId, 0);

        model.addAttribute("messages", items);
        model.addAttribute("site", siteId);
        model.addAttribute("username", username);
        model.addAttribute("counter", Counter.get(Integer.toString(siteId)));
        model.addAttribute("title", site);
        model.addAttribute("token", token);
        return "window";
    }

    @GetMapping("/loading")
    public String loading (@RequestParam(name="site") String site,
                           @RequestParam(name="items") String items, Model model) {
        int num = Integer.parseInt(items);
        int id  = Integer.parseInt(site);

        if (id <= 0 || num <= 0) {
            return "window/empty";
        }

        ArrayList<MessageItem> messages = sqlMessage.selectItems(id, num);
        model.addAttribute("messages", messages);

        return messages == null ? "window/empty" : "window/items";
    }
}
