package com.fit.fit_be.global.auth.token;

import com.fit.fit_be.global.auth.jwt.JwtTokenProvider;
import com.fit.fit_be.global.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    public String refreshAccessToken(String refreshToken, Long memberId) {
        if (!redisUtil.hasKey(refreshToken)) {
            throw new RuntimeException();
        }
        return jwtTokenProvider.createToken(memberId);
    }
}
