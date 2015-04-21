package io.highway.to.urhell.rest;

import io.highway.to.urhell.service.ThunderAdminService;

import javax.inject.Inject;
import javax.inject.Named;
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
@Path("/ThunderAdmin")
@Api(value = "/ThunderAdmin", description = "ThunderAdmin management")
public class ThunderAdminRest {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(ThunderAdminRest.class);
	
	@Inject
	private ThunderAdminService thunderAdminService;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Purge stat By Token")
	@Path("/purgeStatByToken/{token}")
	public Response purgeStatByToken(@PathParam("token") String token) {
		LOG.info("Call purgeStatByToken ");
		thunderAdminService.purgeStatByToken(token);
		return Response.status(Status.ACCEPTED).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Delete app By Token")
	@Path("/deleteThunderAppByToken/{token}")
	public Response deleteThunderAppByToken(@PathParam("token") String token) {
		LOG.info("Call deleteThunderAppByToken ");
		thunderAdminService.deleteThunderAppByToken(token);
		return Response.status(Status.ACCEPTED).build();
	}


	

}
