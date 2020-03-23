/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.clustering;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author laidani.youcef
 */
@ManagedBean
@ViewScoped
public final class Clustering {

    private final String PATH = "/opt/glassfish3/glassfish/bin/";
    private static String ipadress = "";
    private List<Cluster> listeClusters;
    private Cluster cluster;
    private List<Instance> listeInstances;
    private Instance instance;
    
    private String myInstance;

    public void init() {
        listeInstances = new ArrayList<>();
        cluster = new Cluster();
        instance = new Instance();
        myInstance = System.getProperty("com.sun.aas.instanceName");
    }

    public Clustering() {
        init();
    }

    public String executerCommande(String command) {
        String line;
        String resultat = "";
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                resultat += line + "\n";
            }
        } catch (Exception e) {
            resultat = e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Erreur", "Error of command!"));
        }
        return resultat;
    }

    public void findClusters() {
        listeClusters = new ArrayList<>();
        String commandINS = "ssh " + ipadress + " " + PATH + "asadmin list-clusters";
        String resultat = executerCommande(commandINS);
        String[] line = resultat.split("\n");
        for (int i = 0; i < line.length - 1; i++) {
            listeClusters.add(new Cluster(line[i].split("\\s+")[0], line[i].split("\\s+")[1]));
        }
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
//                FacesMessage.SEVERITY_INFO, "Resultat : ", listeClusters.size() + " cluster(s) founds"));
    }

    public void startCluster() {
    }

    public void stopCluster() {
    }

    public List<Instance> findInstances() {
        listeInstances = new ArrayList<>();
        String commandINS = "ssh " + ipadress + " " + PATH + "asadmin list-instances --long=true";
        String resultat = executerCommande(commandINS);
        String[] line = resultat.split("\n");
        for (int i = 1; i < line.length - 1; i++) {
            if (line[i].split("\\s+")[5].equals("not")) {
                listeInstances.add(new Instance(line[i].split("\\s+")[0], line[i].split("\\s+")[1], line[i].split("\\s+")[2], line[i].split("\\s+")[5] + " " + line[i].split("\\s+")[6]));
            } else {
                listeInstances.add(new Instance(line[i].split("\\s+")[0], line[i].split("\\s+")[1], line[i].split("\\s+")[2], line[i].split("\\s+")[5]));
            }
        }
        return listeInstances;
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
//                FacesMessage.SEVERITY_INFO, "Resultat : ", listeInstances.size() + " instance(s) founds"));
    }
    
    public void startInstance(Instance ins) {
        instance = ins;
        listeInstances = new ArrayList<>();
        String commandINS = "ssh " + ipadress + " " + PATH + "asadmin start-instance " + instance.getName();
        String resultat = executerCommande(commandINS);
        String[] line = resultat.split("\n");
        String resultFinal = "";
        for (int i = 0; i < line.length; i++) {
            resultFinal += line[i] + "<br/>";
        }
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
//                FacesMessage.SEVERITY_INFO, "Resultat start : ", resultFinal));
        findInstances();
    }

    public void stopInstance(Instance ins) {
        instance = ins;
        listeInstances = new ArrayList<>();
        String commandINS = "ssh " + ipadress + " " + PATH + "asadmin stop-instance " + instance.getName();
        String resultat = executerCommande(commandINS);
        String[] line = resultat.split("\n");
        String resultFinal = "";
        for (int i = 0; 1 < line.length; i++) {
            resultFinal += line[i] + "<br/>";
        }
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
//                FacesMessage.SEVERITY_INFO, instance.getHost() + " " + instance.getName() + "  Resultat stop : ", resultFinal));
        findInstances();
    }

    public String getIpadress() {
        return ipadress;
    }

    public void setIpadress(String ipadress) {
        this.ipadress = ipadress;
    }

    public List<Cluster> getListeClusters() {
        return listeClusters;
    }

    public void setListeClusters(List<Cluster> listeClusters) {
        this.listeClusters = listeClusters;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public List<Instance> getListeInstances() {
        return listeInstances;
    }

    public void setListeInstances(List<Instance> listeInstances) {
        this.listeInstances = listeInstances;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public String getMyInstance() {
        return myInstance;
    }

    public void setMyInstance(String myInstance) {
        this.myInstance = myInstance;
    }

}
