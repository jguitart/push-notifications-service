package org.jguitart.notification.push;

import org.jguitart.notification.push.dto.ApplicationConfigDto;
import org.jguitart.notification.push.dto.ErrorResponseDto;
import org.jguitart.notification.push.model.Errors;
import org.jguitart.notification.push.service.ApplicationConfigService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("app-config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ApplicationConfigResource {

    @Inject
    ApplicationConfigService applicationConfigService;

    @GET
    @Path("{id}")
    public Response getApplicationConfig(@PathParam("id") Long id) {
        ApplicationConfigDto result = applicationConfigService.get(id);
        if(result == null) {
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder().error(Errors.ERROR_APP_CONFIG_NOT_EXISTS).build();
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponseDto).build();
        }
        return Response.ok(result).build();
    }

    @PUT
    @Path("{id}")
    public Response putApplicationConfig() {
        return null;
    }

    @POST
    @Path("/search")
    public Response searchApplicationConfig() {
        return null;
    }

    @POST
    public Response postApplicationConfig(ApplicationConfigDto request) {

        String error = applicationConfigService.validate(request);
        if(error != null && !error.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponseDto.builder().error(error).build()).build();
        }

        ApplicationConfigDto result = applicationConfigService.create(request);

        return Response.ok(result).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteApplicationConfig() {
        return null;
    }

    @PATCH
    @Path("{id}")
    public Response pathcApplicationConfig() {
        return null;
    }


}
