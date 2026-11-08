package com.amru.maintenanceasset.resource;

//import com.amru.maintenanceasset.entity.Role; // entity role sudah tidak dipakai dan digantikan dari dto
import com.amru.maintenanceasset.dto.*;
import com.amru.maintenanceasset.service.RoleService;

import com.amru.maintenanceasset.exception.InvalidIdException;
import com.amru.maintenanceasset.exception.BusinessRuleException;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.validation.Valid;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.PATCH;

import java.util.List;

@Path("/api/roles")
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {

    @Inject
    RoleService roleService;

    // method get all data roles
    @GET
    public Response getAllRoles() {

        List<RoleResponse> roles = roleService.getAllRoles();

        return Response.ok(roles).build();

    }

    // method get data roles by id
    @GET
    @Path("/{id}")
    public Response getRoleById(@PathParam("id") String id) {

        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new InvalidIdException("ID must be a number: " + id);
        }

        RoleResponse role = roleService.getRoleById(parsedId);

        return Response.ok(role).build();

    }

    // method post create role
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRole(@Valid RoleRequest request) {

        RoleResponse role = roleService.createRole(request);

        return Response.status(Response.Status.CREATED)
                .entity(role)
                .build();
    }

    // method put update role
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRole(
            @PathParam("id") String id,
            @Valid RoleRequest request
    ) {

        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new InvalidIdException("ID must be a number: " + id);
        }

        RoleResponse role = roleService.updateRole(parsedId, request);

        return Response.ok(role).build();
    }

    // method deactivate role alternative delete
    @PATCH
    @Path("/{id}/deactivate")
    public Response deactivateRole(@PathParam("id") Long id) {

        RoleResponse role = roleService.deactivateRole(id);

        return Response.ok(role).build();

    }

    // method reactivate role
    @PATCH
    @Path("/{id}/activate")
    public Response activateRole(@PathParam("id") Long id) {

        RoleResponse role = roleService.activateRole(id);

        return Response.ok(role).build();

    }

    // method get all active role
    @GET
    @Path("/active")
    public Response getActiveRoles() {

        List<RoleResponse> roles = roleService.getActiveRoles();

        return Response.ok(roles).build();

    }

}
