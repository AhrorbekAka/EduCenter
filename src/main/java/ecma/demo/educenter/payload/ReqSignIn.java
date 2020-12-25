package ecma.demo.educenter.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqSignIn {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String password;

}
