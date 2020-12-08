package com.spring.demo.controllers;

import com.spring.demo.dto.AlphabetSoupCreate;
import com.spring.demo.dto.AlphabetSoupListResponse;
import com.spring.demo.models.AlphabetSoupModel;
import com.spring.demo.services.AlphabetSoupInteface;
import com.spring.demo.services.GenerateViewInterface;
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

    @Autowired
    private GenerateViewInterface generateViewInterface;

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
    public String generateViewById(@PathVariable("id") UUID id) {

        AlphabetSoupModel model = this.alphabetSoupInteface.getAlphabetSoupById(id).get();

        String letters = model.getLetters();

        int[] configSoup = new int[3];
        configSoup[0] = model.getH();
        configSoup[1] = model.getW();
        configSoup[2] = 0;

        letters = this.generateViewInterface.generateView(letters, configSoup);

        return letters;
    }

    @DeleteMapping( path = "/{id}" )
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {

        this.alphabetSoupInteface.deleteAlphabetSoup(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

    }

}
