package ru.chat.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import ru.chat.model.user.AppUser;
import ru.chat.model.user.AppUserDAO;
import ru.chat.registration.WebRegistration;
import ru.chat.utils.Checker;

import java.util.Map;

@Controller
public class Web {

    @Autowired
    private WebRegistration registration;

    @Autowired
    private AppUserDAO appUserDAO;

    @GetMapping({"/welcome", "/"})
    public String welcome (Model model) {
        model.addAttribute("SRC", Checker.SERVER + "/main/init.js");
        model.addAttribute("INPUT_SRC",  "<script src=\"" + Checker.SERVER + "/main/init.js" + "\"></script>");

        return "web/welcome";
    }

    @GetMapping(value = "/login")
    public String login (Model model) {
        if (Checker.IsAuthorization()) {
            return "redirect:/welcome";
        }

        return "web/login";
    }

    @GetMapping(value = "/panel")
    public String panel (Model model) {
        AppUser user = appUserDAO.getById(Checker.getAuthorizedUser().getInfo().getId());

        if (user.getValidate() <= 0) {
            return "redirect:/account?error=site";
        }

        String connection = Checker.getConnectionString(user);

        model.addAttribute("connection", connection);
        model.addAttribute("active", user.getActive());

        return "web/panel";
    }

    @GetMapping(value = "/account")
    public String account (Model model) {
        AppUser user = appUserDAO.getById(Checker.getAuthorizedUser().getInfo().getId());

        model.addAttribute("input_name", user.getUsername());
        model.addAttribute("input_url", "http://" + user.getSite());
        model.addAttribute("input_active", user.getActive());

        return "web/account";
    }

    @PostMapping(value = "/account")
    public String accountEdit (@RequestParam Map<String,String> params, Model model) {

        AppUser user = Checker.getAuthorizedUser().getInfo();
        if (!registration.update(user.getId(), params, model)) {
            model.addAttribute("input_active", user.getActive());
            return "web/account";
        }

        return "redirect:/account?change=true";
    }

    @GetMapping(value = "/signup")
    public String signup (Model model) {
        if (Checker.IsAuthorization()) {
            return "redirect:/welcome";
        }
        return "web/signup";
    }

    @PostMapping(value = "/signup")
    public String trySignup (@RequestParam Map<String,String> params, Model model) {
        if (Checker.IsAuthorization()) {
            return "redirect:/welcome";
        }
        if (!registration.run(params, model)) {
            return "web/signup";
        }
        return "redirect:/processing?send=true";
    }

    @GetMapping(value = "/processing")
    public String processing (@RequestParam(name="token", required=false, defaultValue="") String token,
                              @RequestParam(name="email", required=false, defaultValue="") String email,
                              @RequestParam(name="send", required=false, defaultValue="") String send) {
        if (Checker.IsAuthorization()) {
            return "redirect:/welcome";
        }

        int id;

        if (!send.isEmpty() && send.equals("true")) {
            return "web/processing/success";
        }
        if (!token.isEmpty() && !email.isEmpty()) {
            id = appUserDAO.processing(token, email);

            if (id == 0) {
                return "redirect:/welcome";
            }
            else {
                appUserDAO.updateValidate(id);
                return "redirect:/login?reg=true";
            }
        }
        return "redirect:/welcome";
    }

    @PostMapping(value = "/active")
    public String active (@RequestParam(name="active") byte active, Model model) {
        if (active != 0 && active != 1) {
            return "redirect:/welcome";
        }
        AppUser user = Checker.getAuthorizedUser().getInfo();

        user.setActive(active);
        appUserDAO.updateActive(user.getId(), active);

        return "redirect:/account";
    }
}