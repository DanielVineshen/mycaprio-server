package org.everowl.mycaprio.shared.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.everowl.mycaprio.shared.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtService provides methods for handling JWT tokens.
 */
@Service
public class JwtTokenProvider {
    @Value("${token.secretKey}")
    private String secretKey;

    @Value("${token.accessTokenExpiration}")
    private long jwtExpiration;

    @Value("${token.refreshTokenExpiration}")
    private long refreshExpiration;

    @Autowired
    private Environment environment;

    /**
     * Extracts the username from the provided token.
     *
     * @param token the JWT token containing the user information
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JWT token.
     *
     * @param <T>            the type of the claim
     * @param token          the JWT token
     * @param claimsResolver the function to resolve the desired claim from the JWT claims
     * @return the extracted claim of type T
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param userDetails The user for whom the token is being generated.
     * @return The generated JWT token.
     */
    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", userDetails.getUserType());
        claims.put("fullName", userDetails.getFullName());
        return generateToken(claims, userDetails);
    }

    /**
     * Generates a token for the given user with the specified extra claims.
     *
     * @param extraClaims The extra claims to include in the token. These will be added as key-value pairs in the token's claims.
     * @param user        The user for whom the token is being generated.
     * @return The generated token as a string.
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails user
    ) {
        return buildToken(extraClaims, user, jwtExpiration);
    }

    /**
     * Generates a refresh token for the specified user.
     *
     * @param user The user for whom the refresh token is generated.
     * @return The generated refresh token as a String.
     */
    public String generateRefreshToken(
            UserDetails user
    ) {
        return buildToken(new HashMap<>(), user, refreshExpiration);
    }

    /**
     * Builds a token based on the provided extra claims, user, and expiration time.
     *
     * @param extraClaims The additional claims to include in the token. This should be a map of key-value pairs where the key is the claim name and the value is the claim value
     *                    .
     * @param user        The user object for which the token is generated.
     * @param expiration  The expiration time of the token in milliseconds.
     * @return The generated token as a string.
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails user,
            long expiration
    ) {
        long tokenExpiration = expiration;

        if (environment.getActiveProfiles()[0].equals("dev")) {
            tokenExpiration = expiration * 30;
        }

        return Jwts
                .builder()
                .claims(extraClaims)
                .claim("username", user.getUsername())
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Checks if the given token is valid for the provided user details.
     *
     * @param token       the token to be validated
     * @param userDetails the user details to validate against the token
     * @return true if the token is valid for the user details, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if a token has expired.
     *
     * @param token The token to check for expiration.
     * @return True if the token has expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the token.
     *
     * @param token The JWT token from which to extract the expiration date.
     * @return The expiration date extracted from the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param token The JWT token from which to extract the claims.
     * @return The claims extracted from the JWT token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Retrieves the signing key for JWT token verification.
     *
     * @return The signing key for JWT token verification.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}