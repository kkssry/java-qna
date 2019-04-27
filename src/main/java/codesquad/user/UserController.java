package codesquad.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @GetMapping("/joinForm")
    public String joinForm() {
        return "/user/form";
    }

    @PostMapping("")        // ("/") 이렇게 하니 /users/ 로 해야 뜨고 /users 로 하면 안떠서 ("/")가 아닌 ("") 이렇게 했다.
    public String join(User user) {
        UserRepository.getInstance().addUser(user);
        return "redirect:/users";
    }

    @GetMapping("")
    public String userList(Model model) {
        model.addAttribute("users", UserRepository.getInstance().getUsers());
        return "/user/list";
    }

    @GetMapping("/{id}")                                                // 머스타쉬 매개변수와 @PathVariable의 값의 이름은 일치해야 함.
    public String showProfile(@PathVariable long id, Model model) {
        model.addAttribute("userPR", UserRepository
                .getInstance()
                .getUsers()
                .stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null));

        return "/user/profile";
    }


//이거 step1이라 디비 안쓰여

    @GetMapping("/{id}/form")
    public String modifyForm(@PathVariable long id, Model model) {
        model.addAttribute("modifyUser", UserRepository
                .getInstance()
                .getUsers()
                .stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null));
        return "/user/updateForm";
    }

    @PostMapping("/{id}")
    public String modifyUser(@PathVariable long id, Model model,User user) {
        UserRepository.
                getInstance()
                .getUsers()
                .stream()
                .filter(targetUser->targetUser.getId()==id)
                .findFirst()
                .orElse(null)
                .modify(user);

        return "redirect:/users";
    }
}
