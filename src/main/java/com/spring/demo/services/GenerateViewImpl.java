package com.spring.demo.services;

import org.springframework.stereotype.Service;

@Service
public class GenerateViewImpl implements GenerateViewInterface{

    public String generateView(String letters, int[] configSoup){

        String lettersShow = "<Style>table {\n" +
                "   border: 1px solid #000;\n" +
                "}\n" +
                "th, td {\n" +
                "   text-align: center;\n" +
                "   border: 1px solid #000;\n" +
                "   padding: 4px;\n" +
                "}</Style>";

        lettersShow += "<Table>";

        char soup[][] = new char[configSoup[0]][configSoup[1]];

        for (int i = 0; i < soup.length; i++) {
            lettersShow += "<tr>";
            for (int j = 0; j < soup[i].length; j++) {
                lettersShow += "<td> "+letters.charAt(configSoup[2])+" </td>";
                configSoup[2]++;
            }
            lettersShow += "</tr>";
        }

        lettersShow += "</Table>";

        return lettersShow;
    }

}
