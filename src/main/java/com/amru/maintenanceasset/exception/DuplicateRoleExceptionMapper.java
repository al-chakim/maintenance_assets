package com.amru.maintenanceasset.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class DuplicateRoleExceptionMapper implements ExceptionMapper<DuplicateRoleException> {

    @Override
    public Response toResponse(DuplicateRoleException exception) {

        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "error", "request conflict",
                        "message", exception.getMessage()
                )).build();

    }

}
