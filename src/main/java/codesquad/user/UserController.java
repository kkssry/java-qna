package codesquad.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/joinForm")
    public String joinForm() {
        return "/user/form";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "/user/login";
    }

    @PostMapping("/login")
    public String login(String userId, String password, HttpSession session) {
        User loginUser = userRepository.findByUserId(userId);               // 작성자가 쓴 아이디를 db에서 찾아본다.
        if (loginUser == null || !loginUser.isSamePassword(password)) {     // 그런뒤 작성자가 쓴 비밀번호가 일치하는지 확인
            return "/user/login_failed";
        }
        session.setAttribute("sessionedUser",loginUser);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("")        // ("/") 이렇게 하니 /users/ 로 해야 뜨고 /users 로 하면 안떠서 ("/")가 아닌 ("") 이렇게 했다.
    public String join(User user, HttpSession session) {
        User newUser = userRepository.save(user);
        session.setAttribute("sessionedUser", newUser);
        return "redirect:/users";
    }

    @GetMapping("")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/user/list";
    }

    @GetMapping("/{id}")                                                // 머스타쉬 매개변수와 @PathVariable의 값의 이름은 일치해야 함.
    public String showProfile(@PathVariable long id, Model model) {
        model.addAttribute("userPR", userRepository.findById(id).orElse(null));
        return "/user/profile";
    }

    @GetMapping("/{id}/form")
    public String modifyForm(@PathVariable long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("modifyUser", user);
        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String modifyUser(@PathVariable long id,User user) {
        User modifyUser= userRepository.findById(id).orElse(null).modify(user);
        userRepository.save(modifyUser);
        return "redirect:/users";
    }

    @GetMapping("/{id}/updateForm")
    public String isMine(@PathVariable long id, HttpSession session, Model model) {
        Object value = session.getAttribute("sessionedUser");
        User user = (User)value;            // 로그인한 유저

        User targetUser = userRepository.findById(id).orElse(null); // 수정하고자 하는 유저
        if (targetUser.isSameUserId(user.getId())) {
            model.addAttribute("user",targetUser);
            return "/user/checkPassword";
        }

        return "redirect:/users";
    }

    @PostMapping("/{id}/checkUser")
    public String checkUser(@PathVariable long id, String password, Model model) {       //수정하고자 하는 유저의 아이디를 데리고 온다.
        User targetUser = userRepository.findById(id).orElse(null);
        if (targetUser.isSamePassword(password)) {
            model.addAttribute("modifyUser",targetUser);
            return "/user/updateForm";
        }
        return "redirect:/users";
    }

}
