package com.som.footBallPortal.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.som.footBallPortal.model.League;

public interface LeagueRepository extends MongoRepository<League, String> {

	League findBy_id(String league_id);
	
	@Query("{'country.country_id': ?0}")
	League findBy_Country(String country_id);
}
