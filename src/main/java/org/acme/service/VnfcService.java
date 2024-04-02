package org.acme.service;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;
import org.bson.Document;
import org.acme.model.Vnfc;
import io.smallrye.mutiny.Uni;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class VnfcService {

    Config config = new ConfigBuilder()
            .withMasterUrl("https://127.0.0.1:60959")
            .withTrustCerts(true)
            .build();
    @Inject
    ReactiveMongoClient mongoClient;

    public Uni<Void> addVnfc1(@PathParam("namespace") String namespace) throws IOException {
        String toscaFilePath = "D:\\NFVM\\src\\main\\resources\\TOSCA.yaml";

        String toscaContent = new String(Files.readAllBytes(Paths.get(toscaFilePath)));

        Yaml yaml = new Yaml();
        Object parsedObject = yaml.load(toscaContent);

        if (parsedObject instanceof Map) {
            Map<String, Object> toscaMap = (Map<String, Object>) parsedObject;
            Map<String, Object> topologyTemplate = (Map<String, Object>) toscaMap.get("topology_template");
            Map<String, Object> nodeTemplates = (Map<String, Object>) topologyTemplate.get("node_templates");
            Map<String, Object> mongo_deployment = (Map<String, Object>) nodeTemplates.get("mongo_deployment");
            Map<String, Object> properties = (Map<String, Object>) mongo_deployment.get("properties");

            String vnfcInstanceId = (String) properties.get("vnfcInstanceId");
            String vnfcName = (String) properties.get("vnfcName");
            String description = (String) properties.get("description");
            String vduid = (String) properties.get("vduid");
            String vnfcState = (String) properties.get("vnfcState");
            String ip = (String) properties.get("ip");


            Document document = new Document();
            document.append("vnfcInstanceId", vnfcInstanceId);
            document.append("vnfcName", vnfcName);
            document.append("description", description);
            document.append("vduid", vduid);
            document.append("vnfcState", vnfcState);
            document.append("ip", ip);

            return getCollection().insertOne(document)
                    .onItem().ignore().andContinueWithNull();

        }
        return null;
    }

    public Uni<Void> addVnfc2(@PathParam("namespace") String namespace) throws IOException {
        String toscaFilePath = "D:\\NFVM\\src\\main\\resources\\TOSCA.yaml";

        String toscaContent = new String(Files.readAllBytes(Paths.get(toscaFilePath)));

        Yaml yaml = new Yaml();
        Object parsedObject = yaml.load(toscaContent);

        if (parsedObject instanceof Map) {

            Map<String, Object> toscaMap1 = (Map<String, Object>) parsedObject;
            Map<String, Object> topologyTemplate1 = (Map<String, Object>) toscaMap1.get("topology_template");
            Map<String, Object> nodeTemplates1 = (Map<String, Object>) topologyTemplate1.get("node_templates");
            Map<String, Object> mongo_deployment1 = (Map<String, Object>) nodeTemplates1.get("webapp_deployment");
            Map<String, Object> properties1 = (Map<String, Object>) mongo_deployment1.get("properties");

            String vnfcInstanceId1 = (String) properties1.get("vnfcInstanceId");
            String vnfcName1 = (String) properties1.get("vnfcName");
            String description1 = (String) properties1.get("description");
            String vduid1 = (String) properties1.get("vduid");
            String vnfcState1 = (String) properties1.get("vnfcState");
            String ip1 = (String) properties1.get("ip");


            Document document1 = new Document();
            document1.append("vnfcInstanceId", vnfcInstanceId1);
            document1.append("vnfcName", vnfcName1);
            document1.append("description", description1);
            document1.append("vduid", vduid1);
            document1.append("vnfcState", vnfcState1);
            document1.append("ip", ip1);

            return getCollection().insertOne(document1)
                    .onItem().ignore().andContinueWithNull();

//             getCollection().insertOne(document1)
//                    .onItem().ignore().andContinueWithNull();
        }

        return null;
    }

    public Uni<Void> DeleteVnfc(@PathParam("vduid") String vduid){
        Document filter = new Document("vduid", vduid);
        return getCollection().deleteMany(filter)
                .onItem().ignore().andContinueWithNull();
    }

    public Uni<Void> HeaingVnfc(@PathParam("namespace") String namespace){
        try (KubernetesClient client = new DefaultKubernetesClient(config)) {
            // Tắt các pod có nhãn app: "reddit" trong namespace "default"
            client.pods().inNamespace(namespace).withLabel("app", "mongo").delete();
            client.pods().inNamespace(namespace).withLabel("app", "webapp").delete();
            System.out.println("Các pod đã được tắt.");
        }
        return null;
    }
    public Uni<List<Vnfc>> listVnfc(){
        return getCollection()
                .find()
                .map(document -> {
                    Vnfc  vnfc= new Vnfc();
                    vnfc.setVnfcInstanceId(document.getString("vnfcInstanceId"));
                    vnfc.setVnfcName(document.getString("vnfcName"));
                    vnfc.setDescription(document.getString("description"));
                    vnfc.setVduid(document.getString("vduid"));
                    vnfc.setVnfcState(document.getString("vnfcState"));
                    vnfc.setIp(document.getString("ip"));
                    return vnfc;
                }).collect().asList();
    }
    private ReactiveMongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("test").getCollection("vnfc");
    }
}
