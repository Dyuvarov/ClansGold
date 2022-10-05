package com.dyuvarov.coffers.resource;

import com.dyuvarov.coffers.service.UserAddGoldServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/useraddgold")
public class UserAddGoldResource {
    @Inject
    private UserAddGoldServiceImpl addGoldService;

    @POST
    public Response addGold(@QueryParam("userId") long userId,
                            @QueryParam("clanId") long clanId,
                            @QueryParam("gold") int gold) {
        Response response;
        try {
            addGoldService.addGoldToClan(userId, clanId, gold);
            response = Response.ok().build();
        } catch (Exception e) {
            response = Response.serverError().build();
        }
        return response;
    }
}
