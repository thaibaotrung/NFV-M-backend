package org.acme.model;

public class Vnf {
    private String id;
    private String vnfInstanceName;
    private String vnfInstanceDescription;
    private String vnfdid;
    private String vnfProvider;
    private String vnfProductName;
    private String vnfSoftwareVersion;
    private String instantiationState;

    public Vnf(){

    }

    public Vnf(String id, String vnfInstanceName, String vnfInstanceDescription, String vnfdid, String vnfProvider, String vnfProductName, String vnfSoftwareVersion, String instantiationState){
        this.id = id;
        this.vnfInstanceName = vnfInstanceName;
        this.vnfInstanceDescription = vnfInstanceDescription;
        this.vnfdid = vnfdid;
        this.vnfProvider = vnfProvider;
        this.vnfProductName = vnfProductName;
        this.vnfSoftwareVersion = vnfSoftwareVersion;
        this.instantiationState = instantiationState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVnfInstanceName() {
        return vnfInstanceName;
    }

    public void setVnfInstanceName(String vnfInstanceName) {
        this.vnfInstanceName = vnfInstanceName;
    }

    public String getVnfInstanceDescription() {
        return vnfInstanceDescription;
    }

    public void setVnfInstanceDescription(String vnfInstanceDescription) {
        this.vnfInstanceDescription = vnfInstanceDescription;
    }

    public String getVnfdid() {
        return vnfdid;
    }

    public void setVnfdid(String vnfdid) {
        this.vnfdid = vnfdid;
    }

    public String getVnfProvider() {
        return vnfProvider;
    }

    public void setVnfProvider(String vnfProvider) {
        this.vnfProvider = vnfProvider;
    }

    public String getVnfProductName() {
        return vnfProductName;
    }

    public void setVnfProductName(String vnfProductName) {
        this.vnfProductName = vnfProductName;
    }

    public String getVnfSoftwareVersion() {
        return vnfSoftwareVersion;
    }

    public void setVnfSoftwareVersion(String vnfSoftwareVersion) {
        this.vnfSoftwareVersion = vnfSoftwareVersion;
    }

    public String getInstantiationState() {
        return instantiationState;
    }

    public void setInstantiationState(String instantiationState) {
        this.instantiationState = instantiationState;
    }
}
