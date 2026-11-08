package com.amru.maintenanceasset.resource;

import com.amru.maintenanceasset.dto.UserRequest;
import com.amru.maintenanceasset.dto.UserResponse;
import com.amru.maintenanceasset.dto.UserUpdateRequest;
import com.amru.maintenanceasset.service.UserService;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    // get all data users
    @GET
    public Response getAllUsers() {

        List<UserResponse> users = userService.getAllusers();
        return Response.ok(users).build();

    }

    // get data users by id
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {

        UserResponse user = userService.getUserById(id);
        return Response.ok(user).build();

    }

    // store new data user
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser (@Valid UserRequest request) {

        UserResponse user = userService.createUser(request);

        return Response.status(Response.Status.CREATED)
                .entity(user)
                .build();

    }

    //  method put for update data user
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(
            @PathParam("id") Long id,
            @Valid UserUpdateRequest request
    ) {
        UserResponse user = userService.updateUser(id, request);
        return Response.ok(user).build();
    }

    // method patch for deactivate user
    @PATCH
    @Path("/{id}/deactivate")
    public Response deactivateUser(@PathParam("id") Long id) {

        UserResponse user = userService.deactivateUser(id);

        return Response.ok(user).build();

    }

    // method patch for reactivate user
    @PATCH
    @Path("/{id}/activate")
    public Response eactivateUser(@PathParam("id") Long id) {

        UserResponse user = userService.activateUser(id);

        return Response.ok(user).build();

    }

}
