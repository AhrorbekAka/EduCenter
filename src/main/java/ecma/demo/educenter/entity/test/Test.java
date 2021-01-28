package ecma.demo.educenter.entity.test;

import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.Subject;
import ecma.demo.educenter.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Test extends AbsEntity {

    private String title;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Group> groups;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Question> questions;

    public Test(){}

    public Test(String title, List<Group> groups, List<Question> questions) {
        this.title = title;
        this.groups = groups;
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return "Test{" +
                "title='" + title + '\'' +
                ", groups=" + groups +
                ", questions=" + questions +
                '}';
    }
}
