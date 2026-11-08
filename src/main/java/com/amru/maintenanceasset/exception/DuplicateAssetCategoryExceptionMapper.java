
package com.amru.maintenanceasset.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class DuplicateAssetCategoryExceptionMapper
        implements ExceptionMapper<DuplicateAssetCategoryException> {

    @Override
    public Response toResponse(DuplicateAssetCategoryException exception) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("message", exception.getMessage()))
                .build();
    }
}
