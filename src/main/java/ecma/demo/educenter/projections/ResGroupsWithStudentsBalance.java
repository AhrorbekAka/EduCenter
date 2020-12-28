package ecma.demo.educenter.projections;

import ecma.demo.educenter.entity.Subject;
import ecma.demo.educenter.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ResGroupsWithStudentsBalance {

    private UUID id;
    private String name;

    private List<ResStudentWithBalance> students;

    private double payment;
    private List<User> teachers;
    private Subject subject;
    private String description;

}
