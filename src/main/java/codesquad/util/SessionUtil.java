package codesquad.util;

import codesquad.user.User;

import javax.servlet.http.HttpSession;

public class SessionUtil {
    public static boolean isLoginUser(HttpSession session) {
        return session.getAttribute("sessionedUser") != null;   // 로그인했으면 true 아니면  false
    }

    public static User loginUser(HttpSession session) {
        return (User)session.getAttribute("sessionedUser");
    }
}
