package com.spring.demo.controllers;

import com.spring.demo.dto.AlphabetSoupCreate;
import com.spring.demo.dto.AlphabetSoupListResponse;
import com.spring.demo.models.AlphabetSoupModel;
import com.spring.demo.services.AlphabetSoupInteface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/alphabetSoup")
public class AlphabetSoupController {
	
    @Autowired
    private AlphabetSoupInteface alphabetSoupInteface;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<AlphabetSoupListResponse> list() {
        return new ResponseEntity<AlphabetSoupListResponse>(
        		new AlphabetSoupListResponse(this.alphabetSoupInteface.list()),
        		HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<AlphabetSoupCreate> create(@RequestBody AlphabetSoupModel alphabetSoup) {

        alphabetSoup.generateAlphabetSoup();
        
        alphabetSoup.generatePosition();

        this.alphabetSoupInteface.saveAlphabetSoup(alphabetSoup);
        
        return new ResponseEntity<AlphabetSoupCreate>(
        		new AlphabetSoupCreate(alphabetSoup.getId()), HttpStatus.CREATED);
    }

    @GetMapping(path = "/view/{id}")
    @ResponseBody
    public String getById(@PathVariable("id") UUID id) {

        AlphabetSoupModel model = this.alphabetSoupInteface.getAlphabetSoupById(id).get();

        String letters = model.getLetters();

        String lettersShow = "";

        lettersShow += "<Style>table {\n" +
                "   border: 1px solid #000;\n" +
                "}\n" +
                "th, td {\n" +
                "   text-align: center;\n" +
                "   border: 1px solid #000;\n" +
                "}</Style>";

        lettersShow += "<Table>";
        int h = model.getH();
        int w = model.getW();

        int pos = 0;

        char soup[][] = new char[h][w];

        for (int i = 0; i < soup.length; i++) {
            lettersShow += "<tr>";
            for (int j = 0; j < soup[i].length; j++) {
                lettersShow += "<td> "+letters.charAt(pos)+" </td>";
                pos++;
            }
            lettersShow += "</tr>";
        }

        lettersShow += "</Table>";

        return lettersShow;

    }

    @DeleteMapping( path = "/{id}" )
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {

        this.alphabetSoupInteface.deleteAlphabetSoup(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

    }

}
