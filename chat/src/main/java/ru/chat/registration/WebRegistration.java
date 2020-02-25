package ru.chat.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.util.UriComponentsBuilder;

import ru.chat.model.user.AppUser;
import ru.chat.model.user.AppUserDAO;
import ru.chat.utils.Checker;
import ru.chat.utils.Email;

import javax.mail.internet.InternetAddress;
import java.net.InetAddress;
import java.util.Map;
import java.net.URL;
import java.util.UUID;

@Service
public class WebRegistration {
    private String token;
    private String host;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private AppUserDAO appUserDAO;

    public WebRegistration () {

    }

    private void generateToken () {
        token = UUID.randomUUID().toString();

        if (token.length() > 28) {
            token.substring(0, 28);
        }
    }

    private void insertUser (Map<String,String> params) throws Exception {

        boolean res = appUserDAO.insert(
                get(params, "name"),
                get(params, "email"),
                host,
                encrytePassword(get(params, "password")),
                token
        );

        if (!res) {
            throw new Exception();
        }
    }

    private void updateUserField (int id, String field, String sql) throws Exception {

        boolean res = appUserDAO.update(id, field, sql);

        if (!res) {
            throw new Exception();
        }
    }

    private  String encrytePassword (String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public boolean validateRepeats (Map<String,String> params, Model model) {
        AppUser user = appUserDAO.findRepeats(get(params, "email"), host);

        if (user == null) {
            return true;
        }

        if (user.getEmail().equals(get(params, "email"))) {
            model.addAttribute("error", "Данный email адрес уже используется");
        }
        else {
            model.addAttribute("error", "Данный url уже испольуется");
        }
        return false;
    }

    public boolean validateName (String name) {
        return name.isEmpty() || name.length() < 2 || name.length() > 25;
    }

    public boolean validateUrl (String url) {
        try {
            new URL(url);
        } catch (Exception e) {
            return false;
        }

        try {
            host = UriComponentsBuilder.fromUriString(url).build().getHost();

            if (host.isEmpty() || host.length() > 48) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }

        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            return !inetAddress.getHostAddress().isEmpty();
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean validateEmail (String email) {
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validatePassword (String password) {
        return password.isEmpty() || password.length() < 6 || password.length() > 20;
    }

    private boolean validateSecondPassword (String password, String second_password) {
        if (second_password.isEmpty()) {
            return false;
        }
        return password.equals(second_password);
    }

    public boolean run (Map<String,String> params, Model model) {
        set(params, model);
        generateToken();

        if (validateName(get(params, "name"))) {
            model.addAttribute("error", "Ошибка в имени");
            return false;
        }
        if (!validateUrl(get(params, "url"))) {
            model.addAttribute("error", "Ошибка в url адресе");
            return false;
        }
        if (!validateEmail(get(params, "email"))) {
            model.addAttribute("error", "Ошибка в email адресе");
            return false;
        }
        if (validatePassword(get(params, "password"))) {
            model.addAttribute("error", "Ошибка в пароле");
            return false;
        }
        if (!validateSecondPassword(get(params, "password"), get(params, "retry_password"))) {
            model.addAttribute("error", "Ошибка в повторение пароля");
            return false;
        }
        if (!validateRepeats(params, model)) {
            return false;
        }

        Email email = new Email(sender);

        email.setAddress(get(params, "email"));
        email.setTitle("Подвержение регистрации на ресурсе ");
        email.setText(Checker.generateEmailLetter(token, get(params, "email")));

        if (!email.send()) {
            model.addAttribute("error", "Не получается отправить письмо на почту, обратитесь чуть позже");
            return false;
        }

        try {
            insertUser(params);
        }
        catch (Exception e) {
            model.addAttribute("error", "Неполадки на сервере :(");
            return false;
        }

        return true;
    }

    public boolean update (int id, Map<String,String> params, Model model) {

        String what;
        String sql;

        if ((what = get(params, "name")) != null )
        {
            if (validateName(what)) {
                model.addAttribute("error", "Ошибка в имени");
                return false;
            }
            sql = "UPDATE users SET username=? WHERE id=?";
        }
        else if ((what = get(params, "url")) != null)
        {
            if (!validateUrl(what)) {
                model.addAttribute("error", "Ошибка в url адресе");
                return false;
            }
            what = host;
            sql = "UPDATE users SET site=? WHERE id=?";
        }
        else if ((what = get(params, "password")) != null)
        {
            if (validatePassword(what)) {
                model.addAttribute("error", "Ошибка в пароле");
                return false;
            }
            if (!validateSecondPassword(what, get(params, "retry_password"))) {
                model.addAttribute("error", "Ошибка в повторение пароля");
                return false;
            }
            what = encrytePassword(what);
            sql = "UPDATE users SET password=? WHERE id=?";
        }
        else
            return false;
        try {
            updateUserField(id, what, sql);
        }
        catch (Exception e) {
            model.addAttribute("error", "Неполадки на сервере :(");
            return false;
        }
        return true;
    }

    private void set (Map<String,String> params, Model model) {
        model.addAttribute("input_name", get(params, "name"));
        model.addAttribute("input_url", get(params, "url"));
        model.addAttribute("input_email", get(params, "email"));
    }

    private String get (Map<String,String> params, String what) {
        try {
            return params.get(what);
        }
        catch (Exception e) {
            return null;
        }
    }
}