package com.som.footBallPortal.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.som.footBallPortal.model.Team;

public interface TeamsRepository extends MongoRepository<Team, String> {

	Team findBy_id(String team_id);
	
	@Query("{'leagues.league.league_id': ?0}")
	Team findBy_League(String league_id);
}
