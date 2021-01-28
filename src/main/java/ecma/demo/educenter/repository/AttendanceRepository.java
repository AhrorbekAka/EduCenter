package ecma.demo.educenter.repository;

import ecma.demo.educenter.entity.Attendance;
import ecma.demo.educenter.entity.Group;
import ecma.demo.educenter.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    Page<Attendance> findAllByGroupOrderByCreatedAtDesc(Group group, Pageable pageable);

    @Query(value = "select a from Attendance a where a.group = :group and a.student = :student")
    List<Attendance> findAllByGroupAndStudentOrderByCreatedAtDesc(Group group, Student student);

}
