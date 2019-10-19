package com.som.footBallPortal.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.som.footBallPortal.model.Standing;


public interface StandingsRepository extends MongoRepository<Standing, String> {

	@Query("{'league.league_id': ?0}")
	Standing findBy_League(String league_id);
}
