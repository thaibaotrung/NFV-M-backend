package org.acme.service;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import java.io.FileInputStream;

import jakarta.ws.rs.core.Response;
import org.yaml.snakeyaml.Yaml;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import java.io.IOException;
import java.io.InputStream;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.acme.model.Vnfc;
import org.bson.Document;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.model.Vnf;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class VnfService {
    Config config = new ConfigBuilder()
            .withMasterUrl("https://127.0.0.1:60959")
            .withTrustCerts(true)
            .build();


    public Uni<Void> Instantiate(@PathParam("namespace") String namespace){

        try (KubernetesClient client = new DefaultKubernetesClient(config)) {

            Secret secret = client.secrets()
                    .load(VnfService.class.getResourceAsStream("/secret.yaml"))
                    .item();

            client.secrets().inNamespace(namespace).resource(secret).create();

            ConfigMap configMap = client.configMaps()
                    .load(VnfService.class.getResourceAsStream("/mongo-config.yaml"))
                    .item();

            client.configMaps().inNamespace(namespace).resource(configMap).create();


            // Load Deployment YAML Manifest into Java object
            Deployment deploy1 = client.apps().deployments()
                    .load(VnfService.class.getResourceAsStream("/mongo-app.yaml"))
                    .item();
            // Apply it to Kubernetes Cluster
            client.apps().deployments().inNamespace(namespace).resource(deploy1).create();

            Service svc1 = client.services()
                    .load(VnfService.class.getResourceAsStream("/mongo-app-service.yaml"))
                    .item();

            client.services().inNamespace(namespace).resource(svc1).create();
            // Load Deployment YAML Manifest into Java object
            Deployment deploy2 = client.apps().deployments()
                    .load(VnfService.class.getResourceAsStream("/web-app.yaml"))
                    .item();
            // Apply it to Kubernetes Cluster
            client.apps().deployments().inNamespace(namespace).resource(deploy2).create();


            Service svc2 = client.services()
                    .load(VnfService.class.getResourceAsStream("/web-app-service.yaml"))
                    .item();

            client.services().inNamespace(namespace).resource(svc2).create();
            System.out.println("Đã bật VNF thành công");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return Uni.createFrom().voidItem();
    }


    public Uni<Void> Terminate(@PathParam("namespace") String namespace){
        try (KubernetesClient client = new DefaultKubernetesClient(config)) {
            client.apps().deployments().inNamespace(namespace).withName("mongo-deployment").delete();
            client.apps().deployments().inNamespace(namespace).withName("webapp-deployment").delete();
            client.services().inNamespace(namespace).withName("mongo-service").delete();
            client.services().inNamespace(namespace).withName("webapp-service").delete();
            client.secrets().inNamespace(namespace).withName("mongo-secret").delete();
            client.configMaps().inNamespace(namespace).withName("mongo-config").delete();
            System.out.println("Pod đã được tắt.");
        }
        return Uni.createFrom().voidItem();
    }

    public Uni<Void> addVnf(@PathParam("namespace") String namespace) throws IOException {
        String toscaFilePath = "D:\\NFVM\\src\\main\\resources\\TOSCA.yaml";

        String toscaContent = new String(Files.readAllBytes(Paths.get(toscaFilePath)));

        Yaml yaml = new Yaml();
        Object parsedObject = yaml.load(toscaContent);

        if (parsedObject instanceof Map) {
            Map<String, Object> toscaMap = (Map<String, Object>) parsedObject;
            Map<String, Object> topologyTemplate = (Map<String, Object>) toscaMap.get("topology_template");
            Map<String, Object> nodeTemplates = (Map<String, Object>) topologyTemplate.get("node_templates");
            Map<String, Object> vnf001 = (Map<String, Object>) nodeTemplates.get("vnf_001");
            Map<String, Object> properties = (Map<String, Object>) vnf001.get("properties");

            String vnfInstanceName = (String) properties.get("vnfInstanceName");
            String vnfInstanceDescription = (String) properties.get("vnfInstanceDescription");
            String vnfdid = (String) properties.get("vnfdid");
            String vnfProvider = (String) properties.get("vnfProvider");
            String vnfProductName = (String) properties.get("vnfProductName");
            String vnfSoftwareVersion = (String) properties.get("vnfSoftwareVersion");
            String instantiationState = (String) properties.get("instantiationState");


            Document document = new Document();
            document.append("vnfInstanceName", vnfInstanceName);
            document.append("vnfInstanceDescription", vnfInstanceDescription);
            document.append("vnfdid", vnfdid);
            document.append("vnfProvider", vnfProvider);
            document.append("vnfProductName", vnfProductName);
            document.append("vnfSoftwareVersion", vnfSoftwareVersion);
            document.append("instantiationState", instantiationState);

            return getCollection().insertOne(document)
                    .onItem().ignore().andContinueWithNull();
        }

        return null;
    }

    public Uni<Void> DeleteVnf(@PathParam("vnfInstanceName") String vnfInstanceName){
        Document filter = new Document("vnfInstanceName", vnfInstanceName);
        return getCollection().deleteOne(filter)
                .onItem().ignore().andContinueWithNull();
    }

    @Inject
    ReactiveMongoClient mongoClient;

    public Uni<List<Vnf>> listVnf(){
        return getCollection()
                .find()
                .map(document -> {
                    Vnf  vnf= new Vnf();
                    vnf.setId(document.getString("id"));
                    vnf.setVnfInstanceName(document.getString("vnfInstanceName"));
                    vnf.setVnfInstanceDescription(document.getString("vnfInstanceDescription"));
                    vnf.setVnfdid(document.getString("vnfdid"));
                    vnf.setVnfProvider(document.getString("vnfProvider"));
                    vnf.setVnfProductName(document.getString("vnfProductName"));
                    vnf.setVnfSoftwareVersion(document.getString("vnfSoftwareVersion"));
                    vnf.setInstantiationState(document.getString("instantiationState"));
                return vnf;
                }).collect().asList();
    }

    private ReactiveMongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("test").getCollection("vnf");
    }
}
