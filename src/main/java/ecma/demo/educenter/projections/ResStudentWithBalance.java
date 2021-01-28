package ecma.demo.educenter.projections;

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
