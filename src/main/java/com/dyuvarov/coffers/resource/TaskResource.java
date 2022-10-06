package com.dyuvarov.coffers.resource;

import com.dyuvarov.coffers.service.TaskService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/task")
public class TaskResource {

    @Inject
    private TaskService taskService;

    @Path("/complete")
    @POST
    public Response completeTask(@QueryParam("clanId") long clanId, @QueryParam("taskId") long taskId) {
        Response response;
        try {
            taskService.completeTask(clanId, taskId);
            response = Response.ok().build();
        } catch (Exception e) {
            response = Response.serverError().build();
        }
        return response;
    }
}
