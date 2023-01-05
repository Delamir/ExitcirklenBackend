package kea.gruppe1.exitcirklenbackend.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import kea.gruppe1.exitcirklenbackend.exceptions.TokenRefreshException;
import kea.gruppe1.exitcirklenbackend.models.RefreshToken;
import kea.gruppe1.exitcirklenbackend.repositories.EmployeeRepository;
import kea.gruppe1.exitcirklenbackend.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RefreshTokenService {
    @Value("${exitcirklen.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Creates a refresh token on a logged-in user
     * @param userId the logged-in user
     * @return a refresh token
     */
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setEmployee(employeeRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    /**
     * Verifies the refresh tokens expiration
     * @param token the specified refresh token
     * @return refresh token
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    /**
     * Deletes a fresh token
     * @param userId the user connected to the refresh token
     * @return the refresh token key
     */
    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByEmployee(employeeRepository.findById(userId).get());
    }
}