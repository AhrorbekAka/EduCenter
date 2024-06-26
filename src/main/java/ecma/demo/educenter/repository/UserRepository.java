package ecma.demo.educenter.repository;

import ecma.demo.educenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query(nativeQuery = true,
            value = "SELECT ur.role_id " +
                    "FROM user_role ur JOIN users u ON ur.user_id = u.id " +
                    "WHEN u.phone_number = :phoneNumber")
    Optional<List<String>> findRolesByPhoneNumber(String phoneNumber);

    List<User> findAllByEnabledOrderByFirstName(boolean enabled);

}
