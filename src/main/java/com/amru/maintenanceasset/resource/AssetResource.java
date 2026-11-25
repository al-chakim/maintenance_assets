package com.amru.maintenanceasset.resource;

import com.amru.maintenanceasset.dto.AssetRequest;
import com.amru.maintenanceasset.dto.AssetResponse;
import com.amru.maintenanceasset.service.AssetService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/assets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AssetResource {

    @Inject
    AssetService assetService;

    // get all assets
    @GET
    public List<AssetResponse> getAllAssets(
            @QueryParam("status") String status,
            @QueryParam("categoryId") String categoryId,
            @QueryParam("keyword") String keyword
    ) {

        Long parsedCategoryId = parseCategoryId(categoryId);

        return assetService.searchAssets(
                status,
                parsedCategoryId,
                keyword
        );

    }

    // get asset by id
    @GET
    @Path("/{id}")
    public AssetResponse getAssetById(@PathParam("id") Long id) {

        return assetService.getAssetById(id);

    }

    // post create new asset
    @POST
    public Response createAsset(@Valid AssetRequest request) {

        AssetResponse response = assetService.createAsset(request);

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();

    }

    // put update asset
    @PUT
    @Path("/{id}")
    public AssetResponse updateAsset(@PathParam("id") Long id, @Valid AssetRequest request) {

        return assetService.updateAsset(id, request);

    }

    // parse long to string for get method
    private Long parseCategoryId(String categoryId) {

        // if parameter is not provided
        if (categoryId == null) {

            return null;

        }

        // reject empty value
        if (categoryId.isBlank()) {

            throw new BadRequestException(
                    "categoryId must be a positive integer"
            );

        }

        try {

            Long parsedId = Long.valueOf(categoryId.trim());

            // id must be positive
            if (parsedId <= 0) {

                throw new BadRequestException(
                        "categoryId must be greater than zero"
                );

            }

            return parsedId;

        } catch (NumberFormatException exception) {

            throw new BadRequestException(
                    "categoryId must contain a valid integer"
            );

        }

    }

}
