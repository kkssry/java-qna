package codesquad.user;

import codesquad.question.Answer;
import codesquad.question.Question;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable=false, length = 20)
    String userId;

    String password;
    String name;
    String email;

    @OneToMany(mappedBy = "user")
    private List<Question> questions;

    @OneToMany(mappedBy = "user")
    private List<Answer> answers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void inputId(int id) {
        this.id = id;
    }

    public User modify(User user) {
        password = user.password;
        name = user.name;
        email = user.email;
        return this;
    }

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    public boolean isSameUserId(long id) {
        return this.id == id;
    }

    public boolean isMine(User user) {
        return isSameUserId(user.id) && isSamePassword(user.password);
    }

    @Override
    public String toString() {
        return "!!!!!!!!!!!!!!!!!!" +
                "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
