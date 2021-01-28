package ecma.demo.educenter.entity.test;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ecma.demo.educenter.entity.Subject;
import ecma.demo.educenter.entity.attachment.ImageModel;
import ecma.demo.educenter.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Question extends AbsEntity {
    private String question;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ImageModel imageModel;

    @OneToOne(fetch = FetchType.EAGER)
    private Subject subject;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;

    public Question() {
    }

    public Question(String question, Subject subject) {
        this.question = question;
        this.subject = subject;
    }

    public Question(String question, ImageModel imageModel, Subject subject) {
        this.question = question;
        this.imageModel = imageModel;
        this.subject = subject;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ImageModel getImageModel() {
        return this.imageModel;
    }

    public void setImageModel(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public Subject getSubject() {return subject;}

    public void setSubject(Subject subject) {this.subject = subject;}

    public List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", subject"+subject+
                ", answers=" + answers +
                '}';
    }
}


