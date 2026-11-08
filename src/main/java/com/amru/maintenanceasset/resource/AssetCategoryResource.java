package com.amru.maintenanceasset.resource;

import com.amru.maintenanceasset.dto.AssetCategoryRequest;
import com.amru.maintenanceasset.dto.AssetCategoryResponse;
import com.amru.maintenanceasset.service.AssetCategoryService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.PathParam;

import java.util.List;

@Path("/api/asset-categories")
@Produces(MediaType.APPLICATION_JSON)
public class AssetCategoryResource {

    @Inject
    AssetCategoryService assetCategoryService;

    // GET all categories endpoint
    @GET
    public Response getAllCategories() {

        List<AssetCategoryResponse> categories = assetCategoryService.getAllCategories();

        return Response.ok(categories).build();

    }

    // GET active categories endpoint
    @GET
    @Path("/active")
    public Response getActiveCategories() {

        List<AssetCategoryResponse> categories = assetCategoryService.getActiveCategories();

        return Response.ok(categories).build();

    }

    // GET category by ID endpoint
    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {

        AssetCategoryResponse category = assetCategoryService.getCategoryById(id);

        return Response.ok(category).build();

    }

    // POST create category endpoint
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCategory(@Valid AssetCategoryRequest request) {

        AssetCategoryResponse category = assetCategoryService.createCategory(request);

        return Response.status(Response.Status.CREATED)
                .entity(category)
                .build();

    }

    // PUT for update endpoint
    @PUT
    @Path("/{id}")
    public AssetCategoryResponse updateCategory(
            @PathParam("id") Long id,
            @Valid AssetCategoryRequest request
    ) {

        return assetCategoryService.updateCategory(id, request);

    }

    // PATCH for deactivate endpoint
    @PATCH
    @Path("/{id}/deactivate")
    public AssetCategoryResponse deactivateCategory(
            @PathParam("id") Long id
    ) {

        return assetCategoryService.deactivateCategory(id);

    }

    // PATCH for activate endpoint
    @PATCH
    @Path("/{id}/activate")
    public AssetCategoryResponse activateCategory(
            @PathParam("id") Long id
    ) {

        return assetCategoryService.activateCategory(id);

    }

}
