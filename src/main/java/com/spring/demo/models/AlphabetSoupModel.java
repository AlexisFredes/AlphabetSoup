package com.spring.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AlphabetSoupModel {

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    private String words;

    @Column(columnDefinition = "text")
    private String letters;

    private int w = 15; //Ancho de la sopa de letras, por defecto 15
    private int h = 15; //Largo de la sopa de letras, por de defecto 15
    private boolean ltr = true; //Habilitar o deshabilitar palabras que van de izquierda a derecha, por defecto true
    private boolean rtl = false; //Habilitar o deshabilitar palabras que van de derecha a izquierda, por defecto false
    private boolean ttb = true; //Habilitar o deshabilitar palabras que van desde arriba hacia abajo, por defecto true
    private boolean btt = false; //Habilitar o deshabilitar palabras que van desde abajo hacia arriba, por defecto false
    private boolean d = false; //Habilitar o deshabilitar palabras diagonales, por defecto false
    private int sr;
    private int sc;
    private int er;
    private int ec;

    public void generateAlphabetSoup() {

        String letters = "";

        Random rnd = new Random();

        char soup[][] = new char[h][w];

        for (int i = 0; i < soup.length; i++) {
            for (int j = 0; j < soup[i].length; j++) {
                soup[i][j] = (char) ((char) rnd.nextInt(26) + (byte)'a');
                letters += soup[i][j];
            }
        }

        this.letters = letters;
        this.h = h;
        this.w = w;

    }

    public void generatePosition(){

        ArrayList<String> rowOccupied =  new ArrayList<>();

        ArrayList<String> columnOccupied =  new ArrayList<>();

        ArrayList<String> occupiedPositions =  new ArrayList<>();

        ArrayList<ArrayList<String>> infoPositions = new ArrayList<>();

        infoPositions.add(0, occupiedPositions);
        infoPositions.add(1, rowOccupied);
        infoPositions.add(2, columnOccupied);

        String words[] = this.words.replace(" ", "").split("-");

        Random myRand = new Random();

        boolean exit = false;

        for (int a = 0; a < words.length; a++) {

            while (true) {

                int randomInteger = myRand.nextInt(5);

                switch (randomInteger) {
                    case 0:
                        if (this.ltr) {
                            this.letters = generateWordLrt(words[a], infoPositions);
                            exit = true;
                        }
                        break;
                    case 1:
                        if (this.rtl) {
                            this.letters = generateWordRtl(words[a], infoPositions);
                            exit = true;
                        }
                        break;
                    case 2:
                        if (this.ttb) {
                            this.letters = generateWordTtb(words[a], infoPositions);
                            exit = true;
                        }
                        break;
                    case 3:
                        if (this.btt) {
                            this.letters = generateWordBtt(words[a], infoPositions);
                            exit = true;
                        }
                        break;
                    case 4:
                        if (this.d) {
                            this.letters = generateWordD(words[a], infoPositions);
                            exit = true;
                        }
                        break;
                    default :
                        break;
                }

                if (exit){
                    break;
                }
            }

            exit = false;

        }

    }

    private String generateWordLrt(String wordToUse, ArrayList<ArrayList<String>> infoPositions) {

        System.out.println("LRT");

        String infoCheck = "";

        String newLetters = "";

        int posRandom[];

        int pos = 0;

        int r = this.h;
        int c = this.w;

        while(true) {

            boolean exit = false;

            boolean rowCheck = false;

            boolean checkPosition = false;

            if (r < c) {
                posRandom = rowColumRandom(r);
            } else {
                posRandom = rowColumRandom(c);
            }

            if ( (posRandom[1] + wordToUse.length() - 1 ) < c) {
                exit = true;
            }

            rowCheck = checkRowOccupied(posRandom[0], infoPositions.get(1));

            if (rowCheck) {
                infoCheck = "lrt-"+posRandom[0]+"-"+posRandom[1]+"-"+wordToUse;

                checkPosition = checkPosition(infoPositions, infoCheck);
            }

            if (exit && checkPosition && rowCheck) {
                break;
            }
        }

        System.out.println(posRandom[0] + " - " + posRandom[1]);

        int posChar = 0;

        char soup[][] = new char[r][c];

        for (int i = 0; i < soup.length; i++) {
            System.out.print("|");

            for (int j = 0; j < soup[i].length; j++) {

                if (i == posRandom[0] && j == posRandom[1]) {
                    if (posChar != wordToUse.length()){
                        soup[i][j] = wordToUse.charAt(posChar);
                        posChar++;
                        posRandom[1]++;
                        infoPositions.get(0).add(i+""+j+"-"+soup[i][j]);
                    }else{
                        soup[i][j] = this.letters.charAt(pos);
                    }

                } else {
                    soup[i][j] = this.letters.charAt(pos);
                }

                newLetters += soup[i][j];
                System.out.print(soup[i][j] + "|");
                pos++;
            }

            System.out.println("");
        }

        infoPositions.get(1).add(String.valueOf(posRandom[0]));

        return newLetters;

    }

    private String generateWordRtl(String wordToUse, ArrayList<ArrayList<String>> infoPositions) {

        String infoCheck = "";

        System.out.println("RTL");

        String newLetters = "";

        int posRandom[];

        int pos = 0;

        int r = this.h;
        int c = this.w;

        while(true) {

            boolean exit = false;

            boolean rowCheck = false;

            boolean checkPosition = false;

            if (r < c) {
                posRandom = rowColumRandom(r);
            } else {
                posRandom = rowColumRandom(c);
            }

            if ( (posRandom[1] - wordToUse.length() - 1 ) >= 0) {
                exit = true;
            }

            rowCheck = checkRowOccupied(posRandom[0], infoPositions.get(1));

            if (rowCheck){

                infoCheck = "rtl-"+posRandom[0]+"-"+posRandom[1]+"-"+wordToUse;

                checkPosition = checkPosition(infoPositions, infoCheck);

            }

            if (exit && checkPosition && rowCheck) {
                break;
            }

        }

        System.out.println(posRandom[0] + " - " + posRandom[1]);

        posRandom[1] -= wordToUse.length() -1;

        int posChar = wordToUse.length() -1;

        char soup[][] = new char[r][c];

        for (int i = 0; i < soup.length; i++) {
            System.out.print("|");

            for (int j = 0; j < soup[i].length; j++) {

                if (i == posRandom[0] && j == posRandom[1]) {

                    if (posChar != -1){
                        soup[i][j] = wordToUse.charAt(posChar);
                        posChar--;
                        posRandom[1]++;
                        infoPositions.get(0).add(i+""+j+"-"+soup[i][j]);
                    } else{
                        soup[i][j] = this.letters.charAt(pos);
                    }

                } else {
                    soup[i][j] = this.letters.charAt(pos);
                }

                newLetters += soup[i][j];
                System.out.print(soup[i][j] + "|");
                pos++;
            }

            System.out.println("");
        }

        infoPositions.get(1).add(String.valueOf(posRandom[0]));

        return newLetters;

    }

    private String generateWordTtb(String wordToUse, ArrayList<ArrayList<String>> infoPositions) {

        System.out.println("TTB");

        String infoCheck = "";

        String newLetters = "";

        int posRandom[];

        int pos = 0;

        int r = this.h;
        int c = this.w;

        while(true) {
            boolean exit = false;

            boolean columnCheck = false;

            boolean checkPosition = false;

            if (r < c) {
                posRandom = rowColumRandom(r);
            } else {
                posRandom = rowColumRandom(c);
            }

            if ( (posRandom[0] + wordToUse.length() - 1 ) < r) {
                exit = true;
            }

            columnCheck = checkColumnOccupied(posRandom[1], infoPositions.get(2));

            if (columnCheck){

                infoCheck = "ttb-"+posRandom[0]+"-"+posRandom[1]+"-"+wordToUse;

                checkPosition = checkPosition(infoPositions, infoCheck);

            }

            if (exit && checkPosition && columnCheck) {
                break;
            }
        }

        System.out.println(posRandom[0] + " - " + posRandom[1]);

        int posChar = 0;

        char soup[][] = new char[r][c];

        for (int i = 0; i < soup.length; i++) {
            System.out.print("|");

            for (int j = 0; j < soup[i].length; j++) {

                if (i == posRandom[0] && j == posRandom[1]) {

                    if (posChar != wordToUse.length()){
                        soup[i][j] = wordToUse.charAt(posChar);
                        posChar++;
                        posRandom[0]++;
                        infoPositions.get(0).add(i+""+j+"-"+soup[i][j]);
                    }else{
                        soup[i][j] = this.letters.charAt(pos);
                    }

                } else {
                    soup[i][j] = this.letters.charAt(pos);
                }

                newLetters += soup[i][j];
                System.out.print(soup[i][j] + "|");
                pos++;
            }

            System.out.println("");
        }

        infoPositions.get(2).add(String.valueOf(posRandom[1]));

        return newLetters;

    }

    private String generateWordBtt(String wordToUse, ArrayList<ArrayList<String>> infoPositions) {

        System.out.println("BTT");

        String infoCheck = "";

        String newLetters = "";

        int posRandom[];

        int pos = 0;

        int r = this.h;
        int c = this.w;

        while(true) {

            boolean exit = false;

            boolean columnCheck = false;

            boolean checkPosition = false;

            if (r < c) {
                posRandom = rowColumRandom(r);
            } else {
                posRandom = rowColumRandom(c);
            }

            if ( (posRandom[0] - wordToUse.length() - 1 ) >= 0) {
                exit = true;
            }

            columnCheck = checkColumnOccupied(posRandom[1], infoPositions.get(2));

            if (columnCheck){
                infoCheck = "btt-"+posRandom[0]+"-"+posRandom[1]+"-"+wordToUse;

                checkPosition = checkPosition(infoPositions, infoCheck);
            }

            if (exit && checkPosition && columnCheck) {
                break;
            }
        }

        System.out.println(posRandom[0] + " - " + posRandom[1]);

        posRandom[0] -= wordToUse.length() -1;

        int posChar = wordToUse.length() -1;

        char soup[][] = new char[r][c];

        for (int i = 0; i < soup.length; i++) {
            System.out.print("|");

            for (int j = 0; j < soup[i].length; j++) {

                if (i == posRandom[0] && j == posRandom[1]) {

                    if (posChar != -1){
                        soup[i][j] = wordToUse.charAt(posChar);
                        posChar--;
                        posRandom[0]++;
                        infoPositions.get(0).add(i+""+j+"-"+soup[i][j]);
                    } else{
                        soup[i][j] = this.letters.charAt(pos);
                    }

                } else {
                    soup[i][j] = this.letters.charAt(pos);
                }

                newLetters += soup[i][j];
                System.out.print(soup[i][j] + "|");
                pos++;
            }

            System.out.println("");
        }

        infoPositions.get(2).add(String.valueOf(posRandom[1]));

        return newLetters;

    }

    private String generateWordD(String wordToUse, ArrayList<ArrayList<String>> infoPositions) {

        System.out.println("D");

        String newLetters = "";

        String infoCheck = "";

        int posRandom[];

        int pos = 0;

        int r = this.h;
        int c = this.w;

        while(true) {

            boolean exit = false;

            boolean checkPosition = false;

            if (r < c) {
                posRandom = rowColumRandom(r);
            } else {
                posRandom = rowColumRandom(c);
            }

            if ( (posRandom[0] + wordToUse.length() - 1 ) < r && (posRandom[1] + wordToUse.length() - 1) < c) {
                exit = true;
            }

            infoCheck = "d-"+posRandom[0]+"-"+posRandom[1]+"-"+wordToUse;

            checkPosition = checkPosition(infoPositions, infoCheck);

            if (exit && checkPosition) {
                break;
            }
        }

        System.out.println(posRandom[0] + " - " + posRandom[1]);

        int posChar = 0;

        char soup[][] = new char[r][c];

        for (int i = 0; i < soup.length; i++) {
            System.out.print("|");

            for (int j = 0; j < soup[i].length; j++) {

                if (i == posRandom[0] && j == posRandom[1]) {

                    if (posChar != wordToUse.length()){
                        soup[i][j] = wordToUse.charAt(posChar);
                        posChar++;
                        posRandom[0]++;
                        posRandom[1]++;
                        infoPositions.get(0).add(i+""+j+"-"+soup[i][j]);
                    }else{
                        soup[i][j] = this.letters.charAt(pos);
                    }

                } else {
                    soup[i][j] = this.letters.charAt(pos);
                }

                newLetters += soup[i][j];
                System.out.print(soup[i][j] + "|");
                pos++;
            }

            System.out.println("");
        }

        return newLetters;

    }

    public int[] rowColumRandom(int maxRandom){

        Random myRand = new Random();

        int row = myRand.nextInt(maxRandom);
        int colum = myRand.nextInt(maxRandom);

        int posRandom[] = new int[2];

        posRandom[0] = row;
        posRandom[1] = colum;

        return posRandom;

    }

    public boolean checkPosition(ArrayList<ArrayList<String>> infoPositions, String infoCheck) {

        String info[] = infoCheck.split("-");

        boolean exit;

        exit = simulation(info, infoPositions.get(0));

        return exit;
    }

    public boolean checkRowOccupied(int r, ArrayList<String> rowOccupied){
        boolean exit = false;

        if (rowOccupied.toArray().length != 0) {

            for (int a = 0; a < rowOccupied.toArray().length; a++) {

                if (Integer.parseInt(rowOccupied.get(a)) == r) {
                    exit = false;
                    break;
                } else {
                    exit = true;
                }

            }

        } else {
            exit = true;
        }

        return exit;
    }

    public boolean checkColumnOccupied(int c, ArrayList<String> columnOccupied){
        boolean exit = false;

        if (columnOccupied.toArray().length != 0) {

            for (int a = 0; a < columnOccupied.toArray().length; a++) {

                if (Integer.parseInt(columnOccupied.get(a)) == c) {
                    exit = false;
                    break;
                } else {
                    exit = true;
                }

            }

        } else {
            exit = true;
        }

        return exit;
    }

    public boolean simulation(String[] info, ArrayList<String> occupiedPositions){

        int pos = 0;

        int variables[];

        variables = checkType(info);

        boolean exit = true;

        boolean check[] = new boolean[2];

        char soup[][] = new char[this.h][this.w];

        for (int i = 0; i < soup.length; i++) {

            for (int j = 0; j < soup[i].length; j++) {

                if (i == variables[1] && j == variables[2]) {

                    check = checkLetter(i+""+j+"-"+info[3].charAt(variables[0]), occupiedPositions);

                    if (check[0] == false && check[1] == false || check[0] && check[1]) {
                        variables = checkCondition(info, variables);
                        if (variables[3] != 0) {
                            soup[i][j] = info[3].charAt(variables[0]);
                        } else {
                            soup[i][j] = this.letters.charAt(pos);
                        }

                    }else {
                        exit = false;
                        break;
                    }

                }else {
                    soup[i][j] = this.letters.charAt(pos);
                }
                pos++;
            }

            if (exit == false) {
                break;
            }

        }

        return exit;
    }

    public int[] checkType (String[] info){
        int newValue[] = new int[4];

        if (info[0].equals("lrt") || info[0].equals("ttb") || info[0].equals("d")) {

            newValue[0] = 0; //posChar

            newValue[1] = Integer.parseInt(info[1]); //posRow

            newValue[2] = Integer.parseInt(info[2]); //posColumn

        }else if (info[0].equals("rtl")) {

            newValue[0] = info[3].length() - 1; //posChar

            newValue[1] = Integer.parseInt(info[1]); //posRow

            newValue[2] = Integer.parseInt(info[2]) - (info[3].length() - 1); //posColumn
        }else if (info[0].equals("btt")) {

            newValue[0] = info[3].length() - 1; //posChar

            newValue[1] = Integer.parseInt(info[1]) - (info[3].length() - 1); //posRow

            newValue[2] = Integer.parseInt(info[2]); //posColumn

        }

        return newValue;
    }

    public int[] checkCondition(String[] info, int variables[]){
        int newVariables[] = variables;

        newVariables[3] = 0;

        if (info[0].equals("lrt") || info[0].equals("d")) {

            if (newVariables[0] != info[3].length() -1) {
                newVariables[0]++;
                newVariables[2]++;
                if (info[0].equals("d")){
                    newVariables[1]++;
                }
                newVariables[3] = 1;
            }else {
                newVariables[3] = 0;
            }

        }else if (info[0].equals("rtl")) {

            if (newVariables[0] != 0) {
                newVariables[0]--;
                newVariables[2]++;
                newVariables[3] = 1;
            }else {
                newVariables[3] = 0;
            }
        }else if (info[0].equals("ttb")) {
            if (newVariables[0] != info[3].length() -1) {
                newVariables[0]++;
                newVariables[1]++;
                newVariables[3] = 1;
            }else {
                newVariables[3] = 0;
            }
        }else if (info[0].equals("btt")) {
            if (newVariables[0] != 0) {
                newVariables[0]--;
                newVariables[1]++;
                newVariables[3] = 1;
            }else {
                newVariables[3] = 0;
            }
        }

        return newVariables;
    }

    public boolean[] checkLetter(String position, ArrayList<String> occupiedPositions){

        boolean exit[] = new boolean[2];

        String infoToCheck[] = position.split("-");

        if (occupiedPositions.toArray().length != 0) {

            for (int a = 0; a < occupiedPositions.toArray().length; a++) {

                String posToCheck = occupiedPositions.get(a).split("-")[0];
                String letterCheck = occupiedPositions.get(a).split("-")[1];

                if (infoToCheck[0].equals(posToCheck)) {
                    exit[0] = true;
                    if(infoToCheck[1].equals(letterCheck)){
                        exit[1] = true;
                        break;
                    }
                    break;
                }

            }

        }else {
            exit[0] = false;
            exit[1] = false;
        }

        return exit;

    }
}
