package com.fit.fit_be.global.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomCodeGenerator {
    public static String generateCode() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        int randomNumber = 10000 + random.nextInt(90000);
        String code = String.valueOf(randomNumber);
        return code;
    }

}

