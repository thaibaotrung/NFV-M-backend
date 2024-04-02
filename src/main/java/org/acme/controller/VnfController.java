package org.acme.controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.model.Vnf;
import org.acme.service.VnfService;
import org.acme.service.VnfcService;
import java.io.IOException;
import java.util.List;


@Path("/vnf")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VnfController {
    @Inject
    VnfService vnfService;
    @Inject
    VnfcService vnfcService;
    @GET
    public Uni<List<Vnf>> vnfList(){
        return vnfService.listVnf();
    }

    @POST
    @Path("/instantiate/{namespace}")
    public Uni<Void> Instantiate(@PathParam("namespace") String namespace) throws IOException {
        vnfService.Instantiate(namespace);

        return vnfService.addVnf(namespace);
    }

    @POST
    @Path("/terminate/{namespace}")

    public Uni<Void> Terminate(@PathParam("namespace") String namespace) throws  IOException{
        return vnfService.Terminate(namespace);
    }

    @DELETE
    @Path("/terminate/{vnfInstanceName}")

    public Uni<Void> DeleteVnf(@PathParam("vnfInstanceName") String vnfInstanceName) throws IOException{
        return vnfService.DeleteVnf(vnfInstanceName);
    }

    @PUT
    @Path("/healing/{namespace}")

    public Uni<Void> HealingVnf(@PathParam("namespace") String namespace){
        return vnfcService.HeaingVnfc(namespace);
    }
}
