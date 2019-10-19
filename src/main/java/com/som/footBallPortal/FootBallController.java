package com.som.footBallPortal;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.som.footBallPortal.repositories.CountryRepository;
import com.som.footBallPortal.repositories.LeagueRepository;
import com.som.footBallPortal.repositories.StandingsRepository;
import com.som.footBallPortal.repositories.TeamsRepository;

@RestController
@RequestMapping("/")
@Validated
public class FootBallController {
	private static final Logger logger = LogManager.getLogger(FootBallController.class);

	@Autowired
	private CountryRepository countriesRepo;

	@Autowired
	private LeagueRepository leaguesRepo;

	@Autowired
	private TeamsRepository teamsRepo;

	@Autowired
	private StandingsRepository standingsRepo;

	private ObjectMapper mapper;

	@PostConstruct
	private void init() {
		mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody String get(@RequestParam(required = true) String action,
			@RequestParam(required = true) String APIkey, @RequestParam(required = false) String country_id,
			@RequestParam(required = false) String team_id, @RequestParam(required = false) String league_id) {

		try {
			if (action.equals("get_countries"))
				return getCountries(APIkey);
	
			if (action.equals("get_leagues"))
				return getLeagues(APIkey, country_id);
	
			if (action.equals("get_teams"))
				return getTeams(APIkey, team_id, league_id);
	
			if (action.equals("get_standings"))
				return getStandings(APIkey, league_id);
		}
		catch(Exception e) {
			logger.error("Error while fetching data from repository", e);
			return "{ 'error': 'Internal server error'}";
		}

		return "{ 'error': 'API not supported'}";
	}

	private String getCountries(String aPIkey) {

		try {
			return mapper.writeValueAsString(countriesRepo.findAll());
		} catch (JsonProcessingException e) {
			logger.error("Error while fetching countries info", e);
			return "{ 'error': 'Error while fetching countries info'}";
		}
	}

	private String getLeagues(String aPIkey, String country_id) {

		try {
			if (country_id != null && !country_id.isEmpty())
				return mapper.writeValueAsString(leaguesRepo.findBy_Country(country_id));
			else
				return mapper.writeValueAsString(leaguesRepo.findAll());
		} catch (JsonProcessingException e) {
			logger.error("Error while fetching leagues info", e);
			return "{ 'error': 'Error while fetching leagues info'}";
		}
	}

	private String getTeams(String aPIkey, String team_id, String league_id) {

		try {
			if (team_id != null && !team_id.isEmpty())
				return mapper.writeValueAsString(teamsRepo.findBy_id(team_id));
			else if (league_id != null && !league_id.isEmpty())
				return mapper.writeValueAsString(teamsRepo.findBy_League(league_id));
			else
				return "{ 'error': 'Either team id or league id required'}";
		} catch (JsonProcessingException e) {
			logger.error("Error while fetching teams info", e);
			return "{ 'error': 'Error while fetching teams info'}";
		}
	}

	private String getStandings(String aPIkey, String league_id) {
		try {
			if (league_id != null && !league_id.isEmpty())
				return mapper.writeValueAsString(standingsRepo.findBy_League(league_id));
			else
				return "{ 'error': 'league id is mandatory'}";
		} catch (JsonProcessingException e) {
			logger.error("Error while fetching standings info", e);
			return "{ 'error': 'Error while fetching standings info'}";
		}
	}

}
