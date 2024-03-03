package com.fit.fit_be.global.auth.token;

import com.fit.fit_be.global.auth.exception.RefreshTokenExpiredException;
import com.fit.fit_be.global.auth.jwt.JwtTokenProvider;
import com.fit.fit_be.global.common.util.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    public String refreshAccessToken(String refreshToken) {
        if (!redisUtil.hasKey(refreshToken)) {
            throw new RefreshTokenExpiredException();
        }

        Claims claims = jwtTokenProvider.getClaims(refreshToken);
        Long id = claims.get("memberId", Long.class);
        log.info("id = {}", id);
        return jwtTokenProvider.createToken(id);
    }
}
