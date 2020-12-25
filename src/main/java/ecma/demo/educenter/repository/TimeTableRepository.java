package ecma.demo.educenter.repository;

import ecma.demo.educenter.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, UUID> {

//    @Query(value = "SELECT tt.* FROM time_table tt JOIN groups_time_table_list gttl ON tt.id = gttl.time_table_list_id JOIN groups_students gs ON gttl.groups_id = gs.groups_id AND gs.students_id = :studentId AND gs.groups_id = :groupID", nativeQuery = true)
//    List<TimeTable> findAllByStudentIdAndGroupId(UUID studentId, UUID groupId);

    @Query(value = "SELECT SUM(tt.payment_for_this_month) FROM time_table tt INNER JOIN groups_time_tables gttl on tt.id = gttl.time_tables_id AND gttl.groups_id = :groupId", nativeQuery = true)
    double sumMonthlyCostsByGroupId(UUID groupId);

    @Query(nativeQuery = true, value = "SELECT tt.payment_for_this_month" +
            "  FROM time_table tt JOIN groups_time_tables gtt on tt.id = gtt.time_tables_id" +
            "  WHERE gtt.groups_id=:groupId ORDER BY tt.created_at DESC" +
            "  LIMIT 1")
    double findPaymentAmountByMonthAndGroupId(UUID groupId);


}
