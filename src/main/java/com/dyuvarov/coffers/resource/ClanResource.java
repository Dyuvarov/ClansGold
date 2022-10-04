package com.dyuvarov.coffers.resource;

import com.dyuvarov.coffers.model.Clan;
import com.dyuvarov.coffers.service.ClanService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/clan")
public class ClanResource {
    @Inject
    ClanService clanService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") long id) {
        Optional<Clan> clan = clanService.get(id);
        return clan
                .map(c -> Response.ok(c).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
