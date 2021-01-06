package ecma.demo.educenter.behavior;

import ecma.demo.educenter.payload.ApiResponse;

import java.util.UUID;

public interface PaymentService {
    ApiResponse create(double amount, UUID groupId, UUID studentId);
}
