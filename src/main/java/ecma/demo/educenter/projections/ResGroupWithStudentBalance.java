package ecma.demo.educenter.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface ResGroupWithStudentBalance {

    String getName();

    List<ResStudentWithBalance> getStudents();

    interface ResStudentWithBalance {
        @Value("#{target.lastName + ' ' + target.firstName}")
        String getFullName();
        String getPhoneNumber();
        String getParentsNumber();

        Double getBalance();
    }

}
