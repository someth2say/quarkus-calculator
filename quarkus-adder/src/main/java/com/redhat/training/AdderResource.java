package com.redhat.training;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/add")
public class AdderResource implements AdderService {

    @Inject
    @RestClient
    SolverService solverService;

    @GET
    @Path("/{lhs}/{rhs}")
    @Produces(MediaType.TEXT_PLAIN)
    public Float add(@PathParam("lhs") String lhs, @PathParam("rhs") String rhs) {
        Float lResponse ;
        try {
            lResponse = solverService.solve(lhs);
        } catch (Exception e) {
            throw new WebApplicationException(lhs, Response.status(Response.Status.BAD_REQUEST).entity(lhs).build());
        }
        Float rResponse;
        try {
            rResponse = solverService.solve(rhs);
        } catch (Exception e) {
            throw new WebApplicationException(rhs, Response.status(Response.Status.BAD_REQUEST).entity(rhs).build());
        }

        return lResponse+rResponse;
    }

}