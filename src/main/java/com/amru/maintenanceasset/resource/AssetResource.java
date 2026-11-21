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
    public List<AssetResponse> getAllAssets() {

        return assetService.getAllAssets();

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

}
