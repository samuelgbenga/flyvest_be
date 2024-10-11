package com.flyvestmobile.flyvest.mobile.application.repository;

import com.flyvestmobile.flyvest.mobile.application.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {

    @Query(value = """
      select t from VerificationToken t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<VerificationToken> findAllValidTokenByUser(Long id);

    Optional<VerificationToken> findByToken(String token);
}
