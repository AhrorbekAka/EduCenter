package ecma.demo.educenter.repository;

import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findAllByGroup(Group group);

    void deleteAllByGroup(Group group);
}
