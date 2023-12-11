
package com.thriftytech.ecommerce.Repository;

import com.thriftytech.ecommerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsernameOrEmail(String username, String email);

    // Add additional query methods if needed
    Optional<User> findByUsernameOrEmail(String username, String email);
}
