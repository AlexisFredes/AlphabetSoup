package com.spring.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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
                            this.letters = definePosition(words[a], "lrt", infoPositions);
                            exit = true;
                        }
                        break;
                    case 1:
                        if (this.rtl) {
                            this.letters = definePosition(words[a], "rtl", infoPositions);
                            exit = true;
                        }
                        break;
                    case 2:
                        if (this.ttb) {
                            this.letters = definePosition(words[a], "ttb", infoPositions);
                            exit = true;
                        }
                        break;
                    case 3:
                        if (this.btt) {
                            this.letters = definePosition(words[a], "btt", infoPositions);
                            exit = true;
                        }
                        break;
                    case 4:
                        if (this.d) {
                            this.letters = definePosition(words[a], "d", infoPositions);
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

    public String definePosition(String wordToUse, String type, ArrayList<ArrayList<String>> infoPositions) {

        String infoCheck = "";

        String newString = "";

        String[] info;

        int posRandom[];

        boolean exit;

        while(true) {

            boolean checkColumn = false;

            boolean checkRow = false;

            boolean checkPosition = false;

            exit = false;

            if (this.h < this.w) {
                posRandom = rowColumRandom(this.h);
            } else {
                posRandom = rowColumRandom(this.w);
            }

            exit = checkPositionRandom(type, posRandom, wordToUse);

            if (exit) {

                if (type.equals("ttb") || type.equals("btt")) {
                    checkColumn = checkColumnOccupied(posRandom[1], infoPositions.get(2));
                } else if (type.equals("lrt") || type.equals("rtl")) {
                    checkRow = checkRowOccupied(posRandom[0], infoPositions.get(1));
                } else if (type.equals("d")) {
                    checkColumn = true;
                    checkRow = true;
                }

                infoCheck = type + "-" + posRandom[0] + "-" + posRandom[1] + "-" + wordToUse;

                if (checkRow || checkColumn) {
                    checkPosition = checkPosition(infoPositions, infoCheck);
                }
            }

            if (exit && checkPosition) {
                if (checkColumn || checkRow) {
                    exit = true;
                    break;
                }
            }
        }

        if (exit){
            info = infoCheck.split("-");
            newString = addWordAlphabetSoup(info, infoPositions);
        }

        return newString;
    }

    public boolean checkPositionRandom(String type, int[] posRandom, String wordToUse){

        boolean exit = false;

        if (type.equals("lrt")) {
            if ( (posRandom[1] + wordToUse.length() - 1 ) < this.w) {
                exit = true;
            }
        }else if (type.equals("rtl")) {
            if ( (posRandom[1] - wordToUse.length() - 1 ) >= 0) {
                exit = true;
            }
        }else if (type.equals("ttb")) {
            if ( (posRandom[0] + wordToUse.length() - 1 ) < this.h) {
                exit = true;
            }
        }else if (type.equals("btt")) {
            if ( (posRandom[0] - wordToUse.length() - 1 ) >= 0) {
                exit = true;
            }
        }else if (type.equals("d")) {
            if ( (posRandom[0] + wordToUse.length() - 1 ) < this.h && (posRandom[1] + wordToUse.length() - 1) < this.w) {
                exit = true;
            }
        }

        return exit;
    }

    public String addWordAlphabetSoup(String[] info, ArrayList<ArrayList<String>> infoPositions){

        String newLetters = "";

        int pos = 0;

        int variables[];

        variables = checkType(info);

        int initial = 0;

        char soup[][] = new char[this.h][this.w];

        for (int i = 0; i < soup.length; i++) {

            for (int j = 0; j < soup[i].length; j++) {

                if (i == variables[1] && j == variables[2]) {

                    variables = checkCondition(info, variables, initial);

                    if (variables[3] != 0) {
                        initial++;
                        soup[i][j] = info[3].charAt(variables[0]);
                        infoPositions.get(0).add(i+""+j+"-"+soup[i][j]);
                    } else {
                        soup[i][j] = this.letters.charAt(pos);
                    }

                } else {
                    soup[i][j] = this.letters.charAt(pos);
                }

                newLetters += soup[i][j];
                pos++;
            }

        }

        if (info[0].equals("lrt") || info[0].equals("rtl")) {
            infoPositions.get(1).add(info[1]);
        }else if (info[0].equals("ttb") || info[0].equals("btt")) {
            infoPositions.get(2).add(info[2]);
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

        int initial = 0;

        variables = checkType(info);

        boolean exit = true;

        boolean check[] = new boolean[2];

        char soup[][] = new char[this.h][this.w];

        for (int i = 0; i < soup.length; i++) {

            for (int j = 0; j < soup[i].length; j++) {

                if (i == variables[1] && j == variables[2]) {

                    if (initial == 0) {
                        check = checkLetter(i+""+j+"-"+info[3].charAt(variables[0]), occupiedPositions);
                    }else {
                        if (variables[0] != info[3].length() -1) {
                            check = checkLetter(i+""+j+"-"+info[3].charAt(variables[0] + 1), occupiedPositions);
                        }
                    }

                    if (check[0] == false && check[1] == false || check[0] && check[1]) {

                        variables = checkCondition(info, variables, initial);

                        if (variables[3] != 0) {
                            initial++;
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

    public int[] checkCondition(String[] info, int variables[], int initial){
        int newVariables[] = variables;

        newVariables[3] = 0;

        if (info[0].equals("lrt") || info[0].equals("d")) {

            if (newVariables[0] != info[3].length() -1) {
                if (initial != 0) {
                    newVariables[0]++;
                }
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
                if (initial != 0) {
                    newVariables[0]--;
                }
                newVariables[2]++;
                newVariables[3] = 1;
            }else {
                newVariables[3] = 0;
            }
        }else if (info[0].equals("ttb")) {
            if (newVariables[0] != info[3].length() - 1) {
                if (initial != 0) {
                    newVariables[0]++;
                }
                newVariables[1]++;
                newVariables[3] = 1;
            }else {
                newVariables[3] = 0;
            }
        }else if (info[0].equals("btt")) {
            if (newVariables[0] != 0) {
                if (initial != 0) {
                    newVariables[0]--;
                }
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

                if (posToCheck.equals(infoToCheck[0])) {
                    exit[0] = true;
                    if(letterCheck.equals(infoToCheck[1])){
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
