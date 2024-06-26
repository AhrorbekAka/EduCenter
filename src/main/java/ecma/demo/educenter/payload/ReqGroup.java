package ecma.demo.educenter.payload;

import ecma.demo.educenter.entity.Student;
import ecma.demo.educenter.entity.TimeTable;
import ecma.demo.educenter.entity.enums.SubjectName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReqGroup extends Request{

    private String name;

    private List<Student> students;

    private List<TimeTable> timeTables;

    private List<UUID> teacherIdList;

    private boolean isPresent;

    private SubjectName subjectName;

    private double payment;

    private double paymentForThisMonth;

    private String description;

    private Pageable pageable;

}
