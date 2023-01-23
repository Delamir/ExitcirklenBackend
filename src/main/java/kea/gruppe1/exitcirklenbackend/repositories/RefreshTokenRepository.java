package kea.gruppe1.exitcirklenbackend.repositories;

import java.util.Optional;

import kea.gruppe1.exitcirklenbackend.models.Employee;
import kea.gruppe1.exitcirklenbackend.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Finds refresh tokens
     * @param token the specified token
     * @return a refresh token
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Deletes a refresh token connected to an employee
     * @param employee the specified employee
     */
    @Modifying
    int deleteByEmployee(Employee employee);
}