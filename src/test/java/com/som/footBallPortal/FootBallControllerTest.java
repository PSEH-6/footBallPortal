package com.som.footBallPortal;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.som.footBallPortal.model.Country;
import com.som.footBallPortal.repositories.CountryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
public class FootBallControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private CountryRepository countryRepository;

	@Test
	public void getCountries() throws JSONException {

		String countryInJson = "[ {\"country_id\":\"41\", \"country_name\":\"England\" }"
				+ "{\"country_id\":\"68\", \"country_name\":\"Italy\" } ]";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(countryInJson, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("/country", entity, String.class);

		String expectedJson = "[ {\"country_id\":\"41\", \"country_name\":\"England\" }"
				+ "{\"country_id\":\"68\", \"country_name\":\"Italy\" } ]";

		assertEquals(HttpStatus.OK_200, response.getStatusCode());
		JSONAssert.assertEquals(expectedJson, response.getBody(), false);

		verify(countryRepository, times(1)).save(any(Country.class));
	}

}
