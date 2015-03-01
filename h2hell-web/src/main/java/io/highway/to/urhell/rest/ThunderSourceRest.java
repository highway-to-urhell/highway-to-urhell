package io.highway.to.urhell.rest;

import io.highway.to.urhell.service.ThunderLocalSourceService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
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
@Path("/ThunderSource")
@Api(value = "/ThunderSource", description = "ThunderSource management")
public class ThunderSourceRest {
	private static final Logger LOG = LoggerFactory
			.getLogger(ThunderSourceRest.class);
	@Inject
	private ThunderLocalSourceService thunderLocalSourceService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Find findSource")
	@Path("/findSource/{token}/{pathClassMethodName}")
	public Response findSource(@PathParam("token") String token,
			@PathParam("pathClassMethodName") String pathClassMethodName) {
		LOG.info("Call findSource token {} pathClassMethodName {}",token,pathClassMethodName);
		String res = thunderLocalSourceService.findClassAndMethod(token, pathClassMethodName);
		return Response.status(Status.ACCEPTED)
				.entity(res).build();
	}
}
