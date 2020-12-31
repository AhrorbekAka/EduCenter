package ecma.demo.educenter.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUser {

    private UUID id;

    private String firstName;

    private String lastName;

    private String roleName;

    private String phoneNumber;

    private String password;

}
