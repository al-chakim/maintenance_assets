package com.amru.maintenanceasset.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class InvalidIdExceptionMapper implements ExceptionMapper<InvalidIdException> {

    @Override
    public Response toResponse(InvalidIdException exception) {

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "error", "invalid id",
                        "message", exception.getMessage()
                )).build();

    }

}
