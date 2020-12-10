package com.spring.demo.controllers;

import com.spring.demo.constants.Messages;
import com.spring.demo.dto.*;
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

    @Autowired
    private CheckLimitsAlphabetSoupInterface checkLimitsAlphabetSoupInterface;

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<AlphabetSoupListResponseDto> list() {
        return new ResponseEntity<AlphabetSoupListResponseDto>(
        		new AlphabetSoupListResponseDto(this.alphabetSoupInteface.list()),
        		HttpStatus.OK);
    }

    @GetMapping(path = "/view/{id}")
    @ResponseBody
    public String generateViewById(@PathVariable("id") UUID id) {

        AlphabetSoupModel model = this.alphabetSoupInteface.getAlphabetSoupById(id).get();

        String letters = model.getLetters();
        String viewLetters;

        int[] configSoup = new int[3];
        configSoup[0] = model.getH();
        configSoup[1] = model.getW();
        configSoup[2] = 0;

        viewLetters = this.generateViewInterface.generateView(letters, configSoup);

        return viewLetters;

    }

    @GetMapping(path = "/list/{id}")
    @ResponseBody
    public ResponseEntity<Object> getWordListById(@PathVariable("id") UUID id) {

        AlphabetSoupModel model = this.alphabetSoupInteface.getAlphabetSoupById(id).get();

        String Words = "";

        Words = model.getWords();

        return new ResponseEntity<Object>(
                new AlphabetSoupWordsListDto(Words), HttpStatus.OK);

    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody AlphabetSoupModel alphabetSoup) {

        alphabetSoup.generateAlphabetSoup();

        alphabetSoup.generatePosition();

        boolean resultValidateLimits = checkLimitsAlphabetSoupInterface.checkHeightWidth(alphabetSoup.getH(), alphabetSoup.getW());

        if (resultValidateLimits) {
            AlphabetSoupModel alphabetSoupModel = this.alphabetSoupInteface
                    .saveAlphabetSoup(alphabetSoup);
            if (alphabetSoupModel == null) {
                return new ResponseEntity<Object>(new AlphabetSoupCreateErrorDto(Messages.ERROR_INTERNAL_SERVER),
                        HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<Object>(new AlphabetSoupCreateDto(alphabetSoup.getId()),
                        HttpStatus.CREATED);
            }
        }

        return new ResponseEntity<Object>(new AlphabetSoupCreateErrorDto(Messages.ERROR_INVALID_DIMENSIONS),
        HttpStatus.FORBIDDEN);
    }

    @PutMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<CheckWordInAlphabetSoupDto> checkWordById(
            @PathVariable("id") UUID id,
            @RequestBody AlphabetSoupModel alphabetSoup) {

        boolean exit;

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
            exit = false;
        }else {
            model.setLetters(check.get(1));
            this.alphabetSoupInteface.saveAlphabetSoup(model);
            exit = true;
        }

        return new ResponseEntity<CheckWordInAlphabetSoupDto>(
                new CheckWordInAlphabetSoupDto(exit), HttpStatus.OK);
    }

    @DeleteMapping( path = "/{id}" )
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {

        this.alphabetSoupInteface.deleteAlphabetSoup(id);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

    }

}
