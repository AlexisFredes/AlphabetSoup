package com.spring.demo.services;

import org.springframework.stereotype.Service;

@Service
public class CheckLimitsAlphabetSoupImpl implements CheckLimitsAlphabetSoupInterface {

    public static final int LIMIT_MIN = 15;
    public static final int LIMIT_MAX = 80;

    public boolean checkHeightWidth(int heigh, int width){

        if (heigh >= LIMIT_MIN && heigh <= LIMIT_MAX &&
                width >= LIMIT_MIN && width <= LIMIT_MAX) {
            return true;
        }else {
            return false;
        }
    }
}
