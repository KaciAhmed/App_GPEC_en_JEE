/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.clustering;

/**
 *
 * @author laidani.youcef
 */
public class Instance {
    
    private String node;
    private String name;
    private String host;
    private String port;
    private String statut;

    public Instance() {
    }
    
    public Instance(String name, String host, String port, String statut) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.statut = statut;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
