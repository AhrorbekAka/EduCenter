package ecma.demo.educenter.payload.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResTestResultsForTheGroup {
    private UUID studentHistoryId;
    private String studentLastName;
    private String studentFirstName;
    private List<ResTR> resTestResults;
}
