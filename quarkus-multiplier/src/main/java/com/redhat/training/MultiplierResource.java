package com.redhat.training;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.training.service.MultiplierService;
import com.redhat.training.service.SolverService;

@Path("/multiply")
public class MultiplierResource implements MultiplierService {
    final Logger log = LoggerFactory.getLogger(MultiplierResource.class);

    @Inject
    @RestClient
    SolverService solverService;

    @GET
    @Path("/{lhs}/{rhs}")
    @Produces(MediaType.TEXT_PLAIN)
    public Float multiply(@PathParam("lhs") String lhs, @PathParam("rhs") String rhs) {
        log.info("Multiplying {} by {}",lhs, rhs);
        Float lResponse ;
        try {
            lResponse = solverService.solve(lhs);
        } catch (Exception e) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("Unable to multiply "+lhs+".\nReason: "+e.getMessage()).build());

        }
        Float rResponse;
        try {
            rResponse = solverService.solve(rhs);
        } catch (Exception e) {
            throw new WebApplicationException(rhs, Response.status(Response.Status.BAD_REQUEST).entity("Unable to multiply "+rhs+"\nReason: "+e.getMessage()).build());
        }
        return lResponse*rResponse;
    }

}