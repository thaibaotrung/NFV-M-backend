package org.acme.model;

public class Vnfc {
    String vnfcInstanceId;
    String vnfcName;
    String description;
    String vduid;
    String vnfcState;
    String ip;

    public Vnfc(){

    }

    public Vnfc(String id, String vnfcInstanceId, String vnfcName, String description, String vduid, String vnfcState, String ip){
        this.vnfcInstanceId = vnfcInstanceId;
        this.vnfcName = vnfcName;
        this.description = description;
        this.vduid = vduid;
        this.vnfcState = vnfcState;
        this.ip = ip;
    }

    public String getVnfcInstanceId() {
        return vnfcInstanceId;
    }

    public void setVnfcInstanceId(String vnfcInstanceId) {
        this.vnfcInstanceId = vnfcInstanceId;
    }

    public String getVnfcName() {
        return vnfcName;
    }

    public void setVnfcName(String vnfcName) {
        this.vnfcName = vnfcName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVduid() {
        return vduid;
    }

    public void setVduid(String vduid) {
        this.vduid = vduid;
    }

    public String getVnfcState() {
        return vnfcState;
    }

    public void setVnfcState(String vnfcState) {
        this.vnfcState = vnfcState;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
