package com.dyuvarov.coffers.resource;

import com.dyuvarov.coffers.exception.NegativeBalanceException;
import com.dyuvarov.coffers.service.UserAddGoldService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/useraddgold")
public class UserAddGoldResource {
    @Inject
    private UserAddGoldService addGoldService;

    @POST
    public Response addGold(@QueryParam("userId") long userId,
                            @QueryParam("clanId") long clanId,
                            @QueryParam("gold") int gold) {
        Response response;
        try {
            addGoldService.addGoldToClan(userId, clanId, gold);
            response = Response.ok().build();
        } catch (NegativeBalanceException e) {
            response = Response.status(400, e.getMessage()).build();
        }
        catch (Exception e) {
            response = Response.serverError().build();
        }
        return response;
    }
}
