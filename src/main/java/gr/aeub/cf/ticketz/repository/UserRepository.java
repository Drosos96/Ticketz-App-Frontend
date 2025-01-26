package gr.aeub.cf.ticketz.repository;

import gr.aeub.cf.ticketz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Αναζήτηση χρήστη με βάση το username
    Optional<User> findByUsername(String username);

    // Αναζήτηση χρηστών με βάση το username που περιέχει συγκεκριμένη ακολουθία χαρακτήρων (case insensitive)
    List<User> findByUsernameContaining(String username);

    // Αναζήτηση χρηστών με βάση το email που περιέχει συγκεκριμένη ακολουθία χαρακτήρων (case insensitive)
    List<User> findByEmailContaining(String email);

    // Αναζήτηση χρηστών με βάση username και email
    List<User> findByUsernameContainingAndEmailContaining(String username, String email);

}