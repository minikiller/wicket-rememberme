package pl.tomaszdziurko.wicket.service;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import pl.tomaszdziurko.wicket.UserSession;
import pl.tomaszdziurko.wicket.model.User;

import javax.servlet.http.Cookie;

public class SessionProvider {

    public static final int REMEMBER_ME_DURATION_IN_DAYS = 30;
    public static final String REMEMBER_ME_LOGIN_COOKIE = "loginCookie";
    public static final String REMEMBER_ME_PASSWORD_COOKIE = "passwordCookie";

    private UserService userService;
    private CookieService cookieService;

    public SessionProvider(UserService userService, CookieService cookieService) {
        this.userService = userService;
        this.cookieService = cookieService;
    }

    public WebSession createNewSession(Request request) {
        UserSession session = new UserSession(request);

        Cookie loginCookie = cookieService.loadCookie(request, REMEMBER_ME_LOGIN_COOKIE);
        Cookie passwordCookie = cookieService.loadCookie(request, REMEMBER_ME_PASSWORD_COOKIE);

        if(loginCookie != null && passwordCookie != null) {
            User user = userService.findByLoginAndPassword(loginCookie.getValue(), passwordCookie.getValue());

            if(user != null) {
                session.setUser(user);
                session.info("You were automatically logged in.");
            }
        }

        return session;
    }
}
