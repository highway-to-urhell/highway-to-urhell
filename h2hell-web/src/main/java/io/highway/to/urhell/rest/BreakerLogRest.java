package io.highway.to.urhell.rest;

import io.highway.to.urhell.dao.BreakerLogDao;
import io.highway.to.urhell.domain.BreakerLog;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Named
@Path("/BreakerLog")
@Api(value = "/BreakerLog", description = "BreakerLog management")
public class BreakerLogRest {

	private static final Logger LOG = LoggerFactory
			.getLogger(ThunderStatRest.class);;

	@Inject
	private BreakerLogDao breakerLogDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Find all Thunder Stat")
	@Path("/findAllBreaker")
	public Response findAll() {
		LOG.info("Call findAllBreaker ");
		List<BreakerLog> listBreakerLog = breakerLogDao.findAll();
		return Response.status(Status.ACCEPTED).entity(listBreakerLog).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Find all Thunder Stat")
	@Path("/findBreakerWithToken/{token}")
	public Response findBreakerWithToken(@PathParam("token") String token) {
		LOG.info("Call findBreakerWithToken ");
		List<BreakerLog> breaker = breakerLogDao.findByToken(token);
		return Response.status(Status.ACCEPTED).entity(breaker).build();
	}

}
