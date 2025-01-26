package gr.aeub.cf.ticketz.repository;

import gr.aeub.cf.ticketz.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    boolean existsByUserIdAndRoleId(Integer userId, Integer roleId);

    static List<UserRole> findByUserId(Integer userId); // Αφαιρείται το static
}
