package ecma.demo.educenter.projections;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ResGroupsWithStudentsBalance {

    UUID id;
    String name;

    List<ResStudentWithBalance> students;

}
