package com.spring.demo.services;

import org.springframework.stereotype.Service;

@Service
public class CheckLimitsLimitsAlphabetSoup implements CheckLimitsAlphabetSoupInterface {

    public boolean checkHeightWidth(int heigh, int width){

        boolean higher = false;
        boolean less = false;

        boolean exit = false;

        if ( heigh >= 15 && width >= 15 ){
            higher = true;
        }

        if ( heigh <= 80 && width <= 80 ){
            less = true;
        }

        if (higher && less) {
            exit = true;
        }

        return exit;
    }
}
