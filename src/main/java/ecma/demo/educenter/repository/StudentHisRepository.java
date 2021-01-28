package ecma.demo.educenter.repository;

import ecma.demo.educenter.entity.StudentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentHisRepository extends JpaRepository<StudentHistory, UUID> {
    @Query("select sh from StudentHistory sh where sh.student.phoneNumber = :studentPhoneNumber")
    Optional<StudentHistory> findByStudent_phoneNumber(String studentPhoneNumber);
}
