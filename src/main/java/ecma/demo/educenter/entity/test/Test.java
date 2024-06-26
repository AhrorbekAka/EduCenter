package ecma.demo.educenter.entity.test;

import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Test extends AbsEntity {

    private String title;

    private String time;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Group> groups;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Question> questions;

    public Test(){}

    public Test(String title, String time, List<Group> groups, List<Question> questions) {
        this.title = title;
        this.time = time;
        this.groups = groups;
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {return time;}

    public void setTime(String time) {this.time = time;}

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
                ", time='" + time + '\'' +
                ", groups=" + groups +
                ", questions=" + questions +
                '}';
    }
}
