package io.highway.to.urhell.rest;

import io.highway.to.urhell.domain.ThunderApp;
import io.highway.to.urhell.rest.domain.MessageStat;
import io.highway.to.urhell.service.ThunderAppService;
import io.highway.to.urhell.service.ThunderStatService;

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
@Path("/ThunderStat")
@Api(value = "/ThunderStat", description = "ThunderStat management")
public class ThunderStatRest {
	private static final Logger LOG = LoggerFactory
			.getLogger(ThunderStatRest.class);;
	
	@Inject
	private ThunderStatService thunderStatService;
	@Inject
	private ThunderAppService thunderAppService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Find ThunderStat By Token")
	@Path("/findThunderStatByToken/{token}")
	public Response findThunderStatByToken(@PathParam("token") String token) {
		LOG.info("Call findThunderStatByToken ");
		MessageStat ms = thunderStatService.analysisStat(token);
		//TODO dirty clean this
		ThunderApp app = thunderAppService.findAppByToken(token);
		
		ms.setAppName(app.getNameApp());
		return Response.status(Status.ACCEPTED).entity(ms).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Update ThunderStat false positive")
	@Path("/updateThunderStatFalsePositive/{id}/{status}")
	public Response updateThunderStatFalsePositive(@PathParam("id") String id,@PathParam("status") String status) {
		thunderStatService.updateThunderStatFalsePositive(id,status);
		return Response.status(Status.ACCEPTED).build();
	}
	
	

		
}
