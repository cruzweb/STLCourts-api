package svc.data.municipal;

import static org.junit.Assert.*;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.WebAppConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;

import svc.Application;
import svc.models.Municipality;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MunicipalityDAOIntegrationTest {
	
	@Autowired
    private MunicipalityDAO dao;

	@Test
	public void GetMunicipalityByCourtIdSuccessful() {
		Municipality municipality = dao.getByCourtId(1L);

		assertThat(municipality, is(notNullValue()));
		assertThat(municipality.municipality, is("Ballwin"));
	}
	
	@Test
	public void GetMunicipalityByCourtIdUnSuccessful() {
		Municipality municipality = dao.getByCourtId(0L);

		assertThat(municipality, is(nullValue()));
	}
	
	@Test
	public void GetMunicipalityByMunicipalityIdSuccessful() {
		Municipality municipality = dao.getByMunicipalityId(1L);

		assertThat(municipality, is(notNullValue()));
		assertThat(municipality.municipality, is("Ballwin"));
	}
	
	@Test
	public void GetMunicipalityByMunicipalityIdUnSuccessful() {
		Municipality municipality = dao.getByMunicipalityId(0L);

		assertThat(municipality, is(nullValue()));
	}
	
	@Test
	public void GetAllMunicipalitiesSuccessful() {
		List<Municipality> municipalities = dao.getAllMunicipalities();

		assertThat(municipalities, is(notNullValue()));
		assertThat(municipalities.isEmpty(),is(false));
	}
}
