package codesquad.question;

import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {

    private static QuestionRepository questionRepository;
    private List<Question> questions = new ArrayList<>();

    private QuestionRepository() {
    }

    public static QuestionRepository getInstance() {
        if (questionRepository == null) {
            questionRepository = new QuestionRepository();
        }
        return questionRepository;
    }

    public void addQuestion(Question question) {
        question.inputId(questions.size() + 1);
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
