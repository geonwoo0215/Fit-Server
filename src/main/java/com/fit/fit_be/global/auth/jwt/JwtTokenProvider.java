package com.fit.fit_be.global.auth.jwt;

import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final String issuer;
    private final String secretKey;
    private final long expirationSeconds;
    private final long refreshExpirationHours;

    private final MemberRepository memberRepository;

    public JwtTokenProvider(
            @Value("${issuer}") String issuer,
            @Value("${secret-key}") String secretKey,
            @Value("${expiration-seconds}") long expirationSeconds,
            @Value("${refresh-expiration-hours}") long refreshExpirationHours,
            MemberRepository memberRepository
    ) {
        this.issuer = issuer;
        this.secretKey = secretKey;
        this.expirationSeconds = expirationSeconds;
        this.refreshExpirationHours = refreshExpirationHours;
        this.memberRepository = memberRepository;
    }

    public String createToken(Long memberId) {

        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .setClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(expirationSeconds, ChronoUnit.SECONDS)))
                .compact();
    }

    public String createRefreshToken() {

        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .setIssuer(issuer)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(refreshExpirationHours, ChronoUnit.HOURS)))
                .compact();
    }


    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Long memberId = claims.get("memberId", Long.class);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(RuntimeException::new);
        Collection<SimpleGrantedAuthority> authorities = List.of();
        return new UsernamePasswordAuthenticationToken(member, null, authorities);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

}
