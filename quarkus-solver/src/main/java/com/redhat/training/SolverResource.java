package com.redhat.training;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("/solver")
public class SolverResource implements SolverService {

    @Inject
    @RestClient
    AdderService adderService;

    @Inject
    @RestClient
    MultiplierService multiplierService;

    static final Pattern multiplyPattern = Pattern.compile("(.*)\\*(.*)");
    static final Pattern addPattern = Pattern.compile("(.*)\\+(.*)");

    @Override
    @GET
    @Path("{equation}")
    @Produces(MediaType.TEXT_PLAIN)
    public Float solve(@PathParam("equation") String equation) {
        Float result;
        try {
            result = Float.parseFloat(equation);
        } catch (NumberFormatException e) {
            Matcher addMatcher = addPattern.matcher(equation);
            if (addMatcher.matches()) {
                try {
                    result = adderService.add(addMatcher.group(1), addMatcher.group(2));
                } catch (WebApplicationException ex) {
                    throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(equation).build());
                }
            } else {
                Matcher multiplyMatcher = multiplyPattern.matcher(equation);
                if (multiplyMatcher.matches()) {
                    try {
                        result = multiplierService.multiply(multiplyMatcher.group(1), multiplyMatcher.group(2));
                    } catch (WebApplicationException ex) {
                        throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(equation).build());

                    }
                } else {
                    throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(equation).build());
                }
            }
        }
        return result;
    }
}

