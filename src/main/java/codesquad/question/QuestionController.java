package codesquad.question;

import codesquad.user.User;
import codesquad.user.UserRepository;
import codesquad.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import javax.servlet.http.HttpSession;

import static codesquad.util.SessionUtil.isLoginUser;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    public String create(HttpSession session, Model model) {
        if (!SessionUtil.isLoginUser(session)) {
            return "redirect:/users/login";
        }
        model.addAttribute("loginUser", SessionUtil.loginUser(session));
        return "/qna/form";
    }

    @PostMapping("/{userId}")
    public String makeQuestion(Question question, @PathVariable long userId) {
        question.setUser(userRepository.findById(userId).orElse(null));
        questionRepository.save(question);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable long id, Model model) {
        model.addAttribute("question", questionRepository.findById(id).orElse(null));
        return "/qna/show";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable long id, Model model) {
        Question targetQ = questionRepository.findById(id).orElse(null);
        model.addAttribute("updateQuestion", targetQ);
        return "/qna/updateForm";
    }

    @PutMapping("/{id}/update")
    public String update(@PathVariable long id, Question updateQuestion) {
        Question targetQ = questionRepository.findById(id).orElse(null);
        targetQ.update(updateQuestion);
        questionRepository.save(targetQ);
        return "redirect:/questions/{id}";      // return "reidrect:/questions" + id; 이거와 결과같다.
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id, HttpSession session) {
        Question targetQ = questionRepository.findById(id).orElse(null);

        // 1.로그인하지 않았으면 로그인 페이지로
        Object value = session.getAttribute("sessionedUser");
        if (value == null) {
            return "/users/login";
        }
        User user = (User) value;        //로그인한 사용자의 아이디

        if (targetQ == null) {          // 존재하지 않는 질문을 지울때 (어케하는진 모르겠으나 인텔리제이가 널일경우도 처리하라해서 일단 만듦)
            return "redirect:/";
        }

        // 2.자신의 글이 아니면 삭제할수 없다. ( 작성자와 로그인한사람의 이름 일치여부로 동일임을 확인)
        if (!targetQ.getUser().isSameUserId(user.getId())) {
            return "redirect:/questions/{id}";
        }

        // 3.자신의 글이면 삭제한뒤 홈화면으로
        questionRepository.delete(targetQ);
        return "redirect:/";
    }


}
