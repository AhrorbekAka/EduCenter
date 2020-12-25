package ecma.demo.educenter.payload;

import ecma.demo.educenter.entity.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqSignUp {
    private String phoneNumber;
    private String password;
    private String firstName;
    private String lastName;
    private RoleName roleName;
}
