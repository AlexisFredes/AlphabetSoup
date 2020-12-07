package com.spring.demo.services;

import com.spring.demo.models.AlphabetSoupModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlphabetSoupInteface {

    List<AlphabetSoupModel> list();

    AlphabetSoupModel saveAlphabetSoup(AlphabetSoupModel alphabetSoup);

    Optional<AlphabetSoupModel> getAlphabetSoupById(UUID id);

    void deleteAlphabetSoup(UUID id);
}
