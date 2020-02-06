package com.redhat.training;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.training.service.AdderService;
import com.redhat.training.service.SolverService;

@Path("/add")
public class AdderResource implements AdderService {

    final Logger log = LoggerFactory.getLogger(AdderResource.class);
    
    @Inject
    @RestClient
    SolverService solverService;

    @GET
    @Path("/{lhs}/{rhs}")
    @Produces(MediaType.TEXT_PLAIN)
    public Float add(@PathParam("lhs") String lhs, @PathParam("rhs") String rhs) {
        log.info("Adding {} to {}" ,lhs, rhs);
        Float lResponse ;
        try {
            lResponse = solverService.solve(lhs);
        } catch (Exception e) {
            throw new WebApplicationException(lhs, Response.status(Response.Status.BAD_REQUEST).entity("Unable to add "+lhs+"\nReason: "+e.getMessage()).build());
        }
        Float rResponse;
        try {
            rResponse = solverService.solve(rhs);
        } catch (Exception e) {
            throw new WebApplicationException(rhs, Response.status(Response.Status.BAD_REQUEST).entity("Unable to add "+rhs+"\nReason: "+e.getMessage()).build());
        }
        return lResponse+rResponse;
    }

}