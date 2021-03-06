package com.spring.demo.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckWordImpl implements CheckWordInterface {

    public List<String> checkWordInAlphabetSoup(int[] configSoup, List<String> checkInfo) {

        String letters = checkInfo.get(0);

        List<String> allPosition;

        List<String> exit = new ArrayList<>();

        int pos = 0;

        char soup[][] = new char[configSoup[0]][configSoup[1]];

        for (int i = 0; i < soup.length; i++) {
            for (int j = 0; j < soup[i].length; j++) {
                soup[i][j] = letters.charAt(pos);
                pos++;
            }
        }

        allPosition = checkPosition(soup, checkInfo.get(1), configSoup);

        if (allPosition.get(0).equals("false")){
            exit.add(0,"false");
            return exit;
        }else {
            exit.add(0,"true");
            String newLetters = alphabetSoupToUpperCase(allPosition, soup);
            exit.add(1, newLetters);

            return exit;
        }


    }

    public List<String> checkPosition(char soup[][], String words, int[] position) {

        String[] checkWords = words.replace(" ", "").split("-");

        String word = "";

        List<String> allPosition = new ArrayList<>();

        boolean exit;

        int[] conditionVariables;

        if (position[2] == position[4]) {

            conditionVariables = defineSmaller(position[3], position[5]);

            for (int a = conditionVariables[0]; a <= conditionVariables[1]; a++){
                word += soup[position[2]][a];
                allPosition.add(position[2]+"-"+a);
            }

        }else if (position[3] == position[5]){

            conditionVariables = defineSmaller(position[2], position[4]);

            for (int a = conditionVariables[0]; a <= conditionVariables[1]; a++){
                word += soup[a][position[3]];
                allPosition.add(a+"-"+position[3]);
            }

        }else if (position[2] != position[4] && position[3] != position[5]){

            for (int r = position[2]; r <= position[4]; r++){
                word += soup[r][position[3]];
                allPosition.add(r + "-" + position[3]);
                position[3]++;
            }

        }

        exit = checkWord(checkWords, word);

        if (exit) {
            return allPosition;
        }else{
            allPosition.add(0,"false");
            return allPosition;
        }
    }

    public int[] defineSmaller(int pos1, int pos2){

        int[] conditionVariables = new int[2];

        if (pos1 < pos2){
            conditionVariables[0] = pos1;
            conditionVariables[1] = pos2;
        }else{
            conditionVariables[0] = pos2;
            conditionVariables[1] = pos1;
        }

        return conditionVariables;
    }

    public boolean checkWord(String[] checkWords, String word) {

        boolean exit = false;

        String reverser = reverseString(word);

        for (int f = 0; f < checkWords.length; f++) {
            if (checkWords[f].equals(word) || checkWords[f].equals(reverser)) {
                exit = true;
                break;
            }
        }

        return exit;

    }

    public String alphabetSoupToUpperCase(List<String> allPosition, char soup[][]){

        String newLetters = "";
        boolean check = false;

        for (int i = 0; i < soup.length; i++) {
            for (int j = 0; j < soup[i].length; j++) {
                check = checkPositionToUpperCase(allPosition, i, j);
                if(check){
                    newLetters += String.valueOf(soup[i][j]).toUpperCase();
                }else{
                    newLetters += soup[i][j];
                }
            }
        }

        return newLetters;
    }

    public boolean checkPositionToUpperCase(List<String> allPosition, int r, int c) {

        int row = 0;
        int column =0;
        boolean exit = false;

        for(int a = 0; a < allPosition.toArray().length; a++) {

            row = Integer.parseInt(allPosition.get(a).split("-")[0]);
            column = Integer.parseInt(allPosition.get(a).split("-")[1]);

            if (row == r && column == c) {
                exit = true;
                break;
            }
        }

        return exit;
    }

    public String reverseString(String word){
        String reverse = "";

        for (int a = word.length()-1; a >= 0; a--){
            reverse += word.charAt(a);
        }

        return reverse;
    }

}
