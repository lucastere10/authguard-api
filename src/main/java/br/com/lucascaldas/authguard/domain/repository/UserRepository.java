package br.com.lucascaldas.authguard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.lucascaldas.authguard.domain.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
