package io.highway.to.urhell.rest;

import io.highway.to.urhell.domain.ThunderApp;
import io.highway.to.urhell.service.LaunchService;
import io.highway.to.urhell.service.ThunderAppService;

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
@Path("/ThunderApp")
@Api(value = "/ThunderApp", description = "ThunderApp management")
public class ThunderAppRest {

	private static final Logger LOG = LoggerFactory
			.getLogger(ThunderAppRest.class);

	@Inject
	private ThunderAppService thunderAppService;
	@Inject
	private LaunchService launchService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Find all Thunder App")
	@Path("/findAllThunderApp")
	public Response findAll() {
		LOG.info("Call findAllThunder ");
		List<ThunderApp> listThunderApp = thunderAppService.findAll();
		return Response.status(Status.ACCEPTED).entity(listThunderApp).build();
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Launch analysis")
	@Path("/launchAnalysis/{token}")
	public Response launchAnalysis(@PathParam("token") String token) {
		LOG.info("Call launchAnalysis ");
		String msg = launchService.launchAnalysis(token);
		return Response.status(Status.ACCEPTED).entity(msg).build();
	}

	

}
