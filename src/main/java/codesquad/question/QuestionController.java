package codesquad.question;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    @GetMapping("")
    public String create() {
        return "/qna/form";
    }

    @PostMapping("")
    public String makeQuestion(Question question, Model model) {
        QuestionRepository.getInstance().addQuestion(question);
        model.addAttribute("questions", QuestionRepository.getInstance().getQuestions());
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable long id, Model model) {
        model.addAttribute("question",QuestionRepository
                .getInstance()
                .getQuestions()
                .stream()
                .filter(question -> question.getId() == id)
                .findFirst()
                .orElse(null));

        return "/qna/show";
    }
}
