package ecma.demo.educenter.payload;

import ecma.demo.educenter.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqStudent {
    private UUID id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String parentsNumber;
    private String address;
    private List<Group> groups;
    private List<UUID> groupIdList;
    private double balance;
    private UUID groupId;
    private double paymentAmount;
}
