package io.highway.to.urhell.rest;

import io.highway.to.urhell.dao.BreakerLogDao;
import io.highway.to.urhell.dao.ThunderAppDao;
import io.highway.to.urhell.dao.ThunderStatDao;
import io.highway.to.urhell.domain.BreakerLog;
import io.highway.to.urhell.domain.ThunderApp;
import io.highway.to.urhell.domain.ThunderStat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Named
@Path("/Populator")
@Api(value = "/Populator", description = "Populator")
public class PopulatorRest {

	private static final Logger LOG = LoggerFactory
			.getLogger(PopulatorRest.class);
	@Inject
	private ThunderAppDao thunderAppDao;
	@Inject
	private BreakerLogDao breakerLogDao;
	@Inject
	private ThunderStatDao thunderStatDao;
	
	private ThunderApp ta0 = new ThunderApp();
	private ThunderApp ta1 = new ThunderApp();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Launch Populator")
	@Path("/launch")
	public Response populator() {
		LOG.info("Call populator ");
		createThunderApp("A12356789","B12356789");
		createAllThunderStat(ta0);
		createAllThunderStat(ta1);
		createBreaker("A12356789");
		createBreaker("B12356789");
		return Response.status(Status.ACCEPTED).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Reset and launch Populator")
	@Path("/resetAndPopulate")	
	public Response resetAndPopulate() {
		breakerLogDao.deleteAll();
		thunderStatDao.deleteAll();
		thunderAppDao.deleteAll();
		

		
		return populator() ;
	}
	
	private void createAllThunderStat(ThunderApp ta){
		for(int i=0;i<10;i++){
			createThunderStat(ta,"io.highway.to.urhell.method"+i);
		}
		for(int i=0;i<5;i++){
			createThunderStat(ta,"io.highway.to.urhell.othermethod"+i);
		}
		for(int i=0;i<3;i++){
			createThunderStat(ta,"io.highway.to.urhell.totomethod"+i);
		}
	}
	
	@Transactional
	private void createThunderStat(ThunderApp ta, String methodName){
		ThunderStat ts = new ThunderStat();
		ts.setPathClassMethodName(methodName);
		ts.setThunderApp(ta);
		ts.setCount(new Long(0));
		ts.setHttpmethod("GET");
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100);
		ts.setUri("/titi"+randomInt);
		thunderStatDao.save(ts);
	}
	
	
	@Transactional
	private void createBreaker(String key) {
		for(int i=0;i<10;i++){
			createBreakerUnit(key,"method"+i);
		}
		for(int i=0;i<5;i++){
			createBreakerUnit(key,"othermethod"+i);
		}
		for(int i=0;i<3;i++){
			createBreakerUnit(key,"totomethod"+i);
		}
	}
	
	private void createBreakerUnit(String key,String methodName){
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(4);
		for(int j=0;j<randomInt;j++){
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh-mm-ss");
			BreakerLog bl0 = new BreakerLog();
			bl0.setDateIncoming(sdf.format(new Date()));
			bl0.setPathClassMethodName("io.highway.to.urhell."+methodName);
			bl0.setToken(key);
			breakerLogDao.save(bl0);
		}
	}

	@Transactional
	private void createThunderApp(String key1, String key2) {
		// create App 1
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh-mm-ss");
		ta0.setDateCreation(sdf.format(new Date()));
		ta0.setDescription("Fake Description from populator");
		ta0.setNameApp("populatorname");
		ta0.setPathSource("/tmp/src/java/io/42kik/");
		ta0.setToken(key1);
		ta0.setUrlApp("http://localhost:9090");
		ta0.setVersionApp("1.0");
		// create App 1
		ta1.setDateCreation(sdf.format(new Date()));
		ta1.setDescription("Fake Description from populator");
		ta1.setNameApp("populatorname");
		ta1.setPathSource("/tmp/src/java/io/h2h/");
		ta1.setToken(key2);
		ta1.setUrlApp("http://localhost:9091");
		ta1.setVersionApp("1.0");
		thunderAppDao.save(ta0);
		thunderAppDao.save(ta1);
	}

}
