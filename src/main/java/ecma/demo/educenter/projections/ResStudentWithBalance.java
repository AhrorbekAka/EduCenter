package ecma.demo.educenter.projections;

import org.hibernate.annotations.Type;

import java.util.UUID;

public interface ResStudentWithBalance {
    String getLastName();
    UUID getId();
    String getFirstName();
    String getPhoneNumber();
    String getParentsNumber();
    String getAddress();

    Double getBalance();
}
