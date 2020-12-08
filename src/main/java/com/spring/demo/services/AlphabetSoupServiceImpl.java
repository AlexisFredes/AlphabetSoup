package com.spring.demo.services;

import com.spring.demo.models.AlphabetSoupModel;
import com.spring.demo.repositories.AlphabetSoupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AlphabetSoupServiceImpl implements AlphabetSoupInteface{

    @Autowired
    private AlphabetSoupRepository alphabetSoupRepository;

    public List<AlphabetSoupModel> list() {
        return (List<AlphabetSoupModel>) alphabetSoupRepository.findAll();
    }

    @Transactional
    public AlphabetSoupModel saveAlphabetSoup(AlphabetSoupModel alphabetSoup) {
        return alphabetSoupRepository.save(alphabetSoup);
    }

    public Optional<AlphabetSoupModel> getAlphabetSoupById(UUID id) {

        return alphabetSoupRepository.findById(id);
    }

    @Transactional
    public void deleteAlphabetSoup(UUID id) {
        alphabetSoupRepository.deleteById(id);
    }

}
