package com.dyuvarov.coffers.resource;

import com.dyuvarov.coffers.model.GoldTransaction;
import com.dyuvarov.coffers.service.GoldTransactionService;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/transaction")
public class TransactionResource {
    @Inject
    private GoldTransactionService goldTransactionService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") long id, @QueryParam("detailed") boolean detailed) {
        Response response;
        try {
            GoldTransaction transaction = goldTransactionService.findById(id, detailed);
            response = Response.ok(transaction).build();
        } catch (EntityNotFoundException e) {
            response = Response.status(404).build();
        } catch (Exception e) {
            response = Response.serverError().build();
        }
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<? extends GoldTransaction> findAllPageable(@QueryParam("pageNumber") Integer pageNumber,
                                                           @QueryParam("pageSize") Integer pageSize,
                                                           @QueryParam("detailed") boolean detailed) {
        return goldTransactionService.findAllPageable(pageNumber, pageSize, detailed);
    }
}
