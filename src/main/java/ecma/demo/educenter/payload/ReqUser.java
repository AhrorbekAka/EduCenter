package ecma.demo.educenter.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReqUser extends Request{

    private String firstName;

    private String lastName;

    private String roleName;

    private String phoneNumber;

    private String password;

}
