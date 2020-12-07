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

    @GetMapping(path = "/{id}")
    @ResponseBody
    public Optional<AlphabetSoupModel> getById(@PathVariable("id") UUID id) {

        return this.alphabetSoupInteface.getAlphabetSoupById(id);

    }

    @DeleteMapping( path = "/{id}" )
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {

        this.alphabetSoupInteface.deleteAlphabetSoup(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

    }

}
