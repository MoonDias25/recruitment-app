package com.authserver.server.repos;

import com.authserver.server.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, String> {

    Authority save(Authority authority);

    Optional<Authority> findByAuthorityName(String authorityName);
}
