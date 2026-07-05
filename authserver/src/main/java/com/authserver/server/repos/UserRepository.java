package com.authserver.server.repos;

import com.authserver.server.entity.Authority;
import com.authserver.server.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findUserById(String id);

    User save(Authority authority);

    List<User> findAllByAuthorityAuthorityNameIn(List<String> authorities);

    @Query("SELECT u FROM User u WHERE u.authority.authorityName IN :roles AND " +
            "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    Page<User> findEmployeesWithEmailFilter(
            @Param("roles") List<String> roles,
            @Param("email") String email,
            Pageable pageable);
}
