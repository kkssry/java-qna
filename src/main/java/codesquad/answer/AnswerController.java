package codesquad.answer;

import codesquad.qna.Question;
import codesquad.qna.QuestionRepository;
import codesquad.user.User;
import codesquad.utility.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/qna/{questionId}/answers")
public class AnswerController {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @PostMapping("")
    public String create(@PathVariable long questionId, String contents, HttpSession session) {
        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.findById(questionId).orElse(null);
        Answer answer = new Answer(loginUser, question, contents);
        answerRepository.save(answer);
        return String.format("redirect:/qna/%d", questionId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long questionId, @PathVariable long id, HttpSession session) {
        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Optional<Answer> replyOwner = answerRepository.findById(id).filter(answer -> answer.isSameWriter(loginUser));
        if (replyOwner.isPresent()) {
            Answer answer = replyOwner.get();
            answer.changeDeletedTrue();
            answerRepository.save(answer);
        }
        return String.format("redirect:/qna/%d", questionId);
    }

    @GetMapping("/{id}")
    public String modify(@PathVariable long questionId, @PathVariable long id, HttpSession session) {
        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Optional<Answer> replyOwner = answerRepository.findById(id).filter(answer -> answer.isSameWriter(loginUser));
        return "/answer/modify";
    }
}
