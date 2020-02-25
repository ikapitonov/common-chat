package ru.chat.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.chat.model.user.AppUser;
import ru.chat.model.user.UserInfo;

public class Checker {
    public static final String SERVER = "http://localhost:8080";

    public static boolean IsAuthorization () {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    public static UserInfo getAuthorizedUser () {
        return (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getConnectionString (AppUser user) {
        return SERVER + "/window?site=" + user.getSite()
                + "&username=" + user.getUsername()
                + "&token=" + user.getToken();
    }

    public static String generateEmailLetter (String token, String email) {
        return "Здравствуйте, пожалуйста, подтвердите регистрацию, перейдя по ссылке "
                + SERVER + "/processing?token=" + token + "&email=" + email;
    }
}
