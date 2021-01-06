package ecma.demo.educenter.service;

import ecma.demo.educenter.behavior.PaymentService;
import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.Payment;
import ecma.demo.educenter.entity.Student;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.repository.GroupRepository;
import ecma.demo.educenter.repository.PaymentRepository;
import ecma.demo.educenter.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    public PaymentServiceImpl (PaymentRepository paymentRepository, GroupRepository groupRepository, StudentRepository studentRepository) {
        this.paymentRepository = paymentRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public ApiResponse create(double amount, UUID groupId, UUID studentId) {
        try {
            Optional<Group> optionalGroup = groupRepository.findById(groupId);
            Optional<Student> optionalStudent = studentRepository.findById(studentId);
            if (optionalGroup.isPresent() && optionalStudent.isPresent()) {
                paymentRepository.save(new Payment(amount, optionalGroup.get(), optionalStudent.get()));
                return new ApiResponse("Payment saved", true);
            }
            return new ApiResponse("Error student or group not found!!!", false);
        } catch (Exception e) {
            return new ApiResponse("Error occurred!!!", false);
        }
    }
}
