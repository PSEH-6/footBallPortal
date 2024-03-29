package com.som.footBallPortal;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.som.footBallPortal.model.Country;
import com.som.footBallPortal.model.League;
import com.som.footBallPortal.model.Standing;
import com.som.footBallPortal.model.Team;
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
	
	@RequestMapping(value = "country", method = RequestMethod.POST)
	public Country createCountry(@Valid @RequestBody Country country) {
		country.set_id(ObjectId.get());
		countriesRepo.save(country);

		logger.info("Created new country with Id: " + country.getCountry_id());
		return country;
	}
	
	@RequestMapping(value = "league", method = RequestMethod.POST)
	public League createLeague(@Valid @RequestBody League league) {
		league.set_id(ObjectId.get());
		leaguesRepo.save(league);

		logger.info("Created new league with Id: " + league.getLeague_id());
		return league;
	}

	@RequestMapping(value = "team", method = RequestMethod.POST)
	public Team createTeam(@Valid @RequestBody Team team) {
		team.set_id(ObjectId.get());
		teamsRepo.save(team);

		logger.info("Created new team with Id: " + team.getTeam_id());
		return team;
	}
	
	@RequestMapping(value = "standings", method = RequestMethod.POST)
	public Standing createLeague(@Valid @RequestBody Standing standing) {
		standing.set_id(ObjectId.get());
		standingsRepo.save(standing);

		logger.info("Created new standing with Id: " + standing.getCountry().getCountry_id() + ":" 
		+ standing.getLeague().getLeague_id() + ":" + standing.getTeam().getTeam_id());
		
		return standing;
	}
}
