package com.spring.demo.controllers;

import com.spring.demo.dto.AlphabetSoupCreate;
import com.spring.demo.dto.AlphabetSoupListResponse;
import com.spring.demo.dto.AlphabetSoupWordsList;
import com.spring.demo.models.AlphabetSoupModel;
import com.spring.demo.services.*;
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

    @Autowired
    private CheckWordInterface checkWordInterface;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<AlphabetSoupListResponse> list() {
        return new ResponseEntity<AlphabetSoupListResponse>(
        		new AlphabetSoupListResponse(this.alphabetSoupInteface.list()),
        		HttpStatus.OK);
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

    @GetMapping(path = "/list/{id}")
    @ResponseBody
    public ResponseEntity<AlphabetSoupWordsList> getWordListById(@PathVariable("id") UUID id) {

        AlphabetSoupModel model = this.alphabetSoupInteface.getAlphabetSoupById(id).get();

        String Words = model.getWords();

        return new ResponseEntity<AlphabetSoupWordsList>(
                new AlphabetSoupWordsList(Words), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<AlphabetSoupCreate> create(@RequestBody AlphabetSoupModel alphabetSoup) {

        alphabetSoup.generateAlphabetSoup();

        alphabetSoup.generatePosition();

        this.alphabetSoupInteface.saveAlphabetSoup(alphabetSoup);

        return new ResponseEntity<AlphabetSoupCreate>(
                new AlphabetSoupCreate(alphabetSoup.getId()), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    @ResponseBody
    public boolean checkWordById(
            @PathVariable("id") UUID id,
            @RequestBody AlphabetSoupModel alphabetSoup) {

        AlphabetSoupModel model = this.alphabetSoupInteface.getAlphabetSoupById(id).get();

        List<String> checkInfo = new ArrayList<>();

        checkInfo.add(model.getLetters());
        checkInfo.add(model.getWords());

        int configSoup[] = new int[6];
        configSoup[0] = model.getH();
        configSoup[1] = model.getW();
        configSoup[2] = alphabetSoup.getSr();
        configSoup[3] = alphabetSoup.getSc();
        configSoup[4] = alphabetSoup.getEr();
        configSoup[5] = alphabetSoup.getEc();

        List<String> check;

        check = this.checkWordInterface.checkWordInAlphabetSoup(configSoup, checkInfo);

        if (check.get(0).equals("false")){
            return false;
        }else {
            model.setLetters(check.get(1));
            this.alphabetSoupInteface.saveAlphabetSoup(model);
            return true;
        }

        //return new ResponseEntity<AlphabetSoupWordsList>(
               // new AlphabetSoupWordsList(Words), HttpStatus.OK);
    }

    @DeleteMapping( path = "/{id}" )
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {

        this.alphabetSoupInteface.deleteAlphabetSoup(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

    }

}
