package codesquad.home;

import codesquad.question.QuestionRepository;
import codesquad.user.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("questions", QuestionRepository.getInstance().getQuestions());
        model.addAttribute("users", UserRepository.getInstance().getUsers());
        return "/index";
    }
}
