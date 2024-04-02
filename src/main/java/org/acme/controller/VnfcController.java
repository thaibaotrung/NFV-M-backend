package org.acme.controller;

import io.smallrye.mutiny.Uni;

import java.io.IOException;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.model.Vnfc;
import org.acme.service.VnfcService;


@Path("/vnfc")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VnfcController {
    @Inject VnfcService vnfcService;
    @GET

    public Uni<List<Vnfc>> vnfcList(){
        return vnfcService.listVnfc();
    }

    @POST
    @Path("/instantiate/{namespace}")
    public Uni<Void> Instantiate(@PathParam("namespace") String namespace) throws IOException {
          return vnfcService.addVnfc1(namespace);
    }

    @POST
    @Path("/instantiate2/{namespace}")
    public Uni<Void> Instantiate2(@PathParam("namespace") String namespace) throws IOException {
        return vnfcService.addVnfc2(namespace);
    }

    @DELETE
    @Path("/terminate/{vduid}")

    public Uni<Void> DeleteVnf(@PathParam("vduid") String vduid) throws IOException{
        return vnfcService.DeleteVnfc(vduid);
    }
}
