package com.fit.fit_be.global.auth.jwt;

import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.repository.MemberRepository;
import com.fit.fit_be.global.common.util.RedisUtil;
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
    private final long expirationHours;
    private final long refreshExpirationHours;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;

    public JwtTokenProvider(
            @Value("${issuer}") String issuer,
            @Value("${secret-key}") String secretKey,
            @Value("${expiration-hours}") long expirationHours,
            @Value("${refresh-expiration-hours}") long refreshExpirationHours,
            RedisUtil redisUtil,
            MemberRepository memberRepository

    ) {
        this.issuer = issuer;
        this.secretKey = secretKey;
        this.expirationHours = expirationHours;
        this.refreshExpirationHours = refreshExpirationHours;
        this.redisUtil = redisUtil;
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
                .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))
                .compact();
    }

    public String createRefreshToken(Long memberId) {

        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        String refreshToken = Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
                .setIssuer(issuer)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(refreshExpirationHours, ChronoUnit.HOURS)))
                .compact();

        redisUtil.setData(refreshToken, memberId.toString(), refreshExpirationHours);

        return refreshToken;
    }

    public void deleteRefreshToken(Long memberId) {
        redisUtil.delete(memberId.toString());
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
