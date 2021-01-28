package ecma.demo.educenter.payload.test;

import ecma.demo.educenter.entity.enums.SubjectName;
import ecma.demo.educenter.payload.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqTest extends Request {
    private UUID id;
    private String title;
    private List<UUID> groupIdList;
    private List<ReqQuestion> reqQuestionList;
}
