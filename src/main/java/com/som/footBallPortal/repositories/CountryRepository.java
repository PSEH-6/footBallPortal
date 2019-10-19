package com.som.footBallPortal.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.som.footBallPortal.model.Country;


public interface CountryRepository extends MongoRepository<Country, String> {

	Country findBy_id(String country_id);
}
