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
@Table(name = "alphabetSoup")
public class AlphabetSoupModel {

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    private String word;

    @Column(columnDefinition = "text")
    private String letters;

    private int w = 15; //Ancho de la sopa de letras, por defecto 15
    private int h = 15; //Largo de la sopa de letras, por de defecto 15
    private boolean ltr = true; //Habilitar o deshabilitar palabras que van de izquierda a derecha, por defecto true
    private boolean rtl = false; //Habilitar o deshabilitar palabras que van de derecha a izquierda, por defecto false
    private boolean ttb = true; //Habilitar o deshabilitar palabras que van desde arriba hacia abajo, por defecto true
    private boolean btt = false; //Habilitar o deshabilitar palabras que van desde abajo hacia arriba, por defecto false
    private boolean d = false; //Habilitar o deshabilitar palabras diagonales, por defecto false


    public void generateAlphabetSoup() {

        String letters = "";

        List<String> conteiner = new ArrayList<>();

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

        String words[] = this.word.replace(" ", "").split("-");

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
                            this.letters = generateWordTtb(words[a]);
                            exit = true;
                        }
                        break;
                    case 3:
                        if (this.btt) {
                            this.letters = generateWordBtt(words[a]);
                            exit = true;
                        }
                        break;
                    case 4:
                        if (this.d) {
                            this.letters = generateWordD(words[a]);
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

            if (exit && rowCheck) {
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

            if (exit && rowCheck) {
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

    private String generateWordTtb(String wordToUse) {

        System.out.println("TTB");

        String newLetters = "";

        int posRandom[];

        int pos = 0;

        int r = this.h;
        int c = this.w;

        boolean exit = false;

        while(true) {
            if (r < c) {
                posRandom = rowColumRandom(r);
            } else {
                posRandom = rowColumRandom(c);
            }

            if ( (posRandom[0] + wordToUse.length() - 1 ) < r) {
                exit = true;
            }

            if (exit) {
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

    private String generateWordBtt(String wordToUse) {

        System.out.println("BTT");

        String newLetters = "";

        int posRandom[];

        int pos = 0;

        int r = this.h;
        int c = this.w;

        boolean exit = false;

        while(true) {
            if (r < c) {
                posRandom = rowColumRandom(r);
            } else {
                posRandom = rowColumRandom(c);
            }

            if ( (posRandom[0] - wordToUse.length() - 1 ) >= 0) {
                exit = true;
            }

            if (exit) {
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

        return newLetters;

    }

    private String generateWordD(String wordToUse) {

        System.out.println("D");

        String newLetters = "";

        int posRandom[];

        int pos = 0;

        int r = this.h;
        int c = this.w;

        boolean exit = false;

        while(true) {
            if (r < c) {
                posRandom = rowColumRandom(r);
            } else {
                posRandom = rowColumRandom(c);
            }

            if ( (posRandom[0] + wordToUse.length() - 1 ) < r && (posRandom[1] + wordToUse.length() - 1) < c) {
                exit = true;
            }

            if (exit) {
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

    private int[] rowColumRandom(int maxRandom){

        Random myRand = new Random();

        int row = myRand.nextInt(maxRandom);
        int colum = myRand.nextInt(maxRandom);

        int posRandom[] = new int[2];

        posRandom[0] = row;
        posRandom[1] = colum;

        return posRandom;

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

}
