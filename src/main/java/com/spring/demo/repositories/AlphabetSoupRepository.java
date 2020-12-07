package com.spring.demo.repositories;

import com.spring.demo.models.AlphabetSoupModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlphabetSoupRepository extends CrudRepository<AlphabetSoupModel, UUID> {

}
