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
    public String modifyUser(@PathVariable long id,User user) {
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

    @GetMapping("/{id}/passCheck")
    public String checkPassForm(@PathVariable long id, Model model) {       // user리스트에서 내가 만약 다른 사람의 수정버튼을 누르면
        User targetUser = UserRepository.getInstance()                      // 다른사람의 정보를 가져와서 비밀번호 창까지 정보를 가져온다.
                .getUsers()
                .stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);

        model.addAttribute("users",targetUser);
        return "/user/checkPassword";
    }

    @PostMapping("/{id}/checkUser")
    public String checkUser(@PathVariable long id, String password, Model model) {       // 가져온 사람의 정보와 내가 입력한 비밀번호를 비교한다!!
        User targetUser = UserRepository.getInstance()
                .getUsers()
                .stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);

        if (targetUser.isMine(password)) {
            model.addAttribute("modifyUser",targetUser);
            return "/user/updateForm";
        }
        return "redirect:/users";
    }
}
