package com.spring.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Entity
@Table(name = "alphabetSoup")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
