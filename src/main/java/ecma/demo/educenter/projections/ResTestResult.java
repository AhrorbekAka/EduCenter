package ecma.demo.educenter.projections;

import ecma.demo.educenter.payload.test.ResTR;

import java.util.List;

public interface ResTestResult {
    String getStudentLastName();
    String getStudentFirstName();
    List<ResTR> getResTestResults();
}
