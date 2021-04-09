package edu.pucmm.eict.Controllers;

import org.eclipse.jetty.websocket.api.Session;

public class ConnectionsController {

    private Session conection;
    private String idConection;

    public ConnectionsController(Session conection, String idConection) {
        this.conection = conection;
        this.idConection = idConection;
    }

    public Session getConection() {
        return conection;
    }

    public void setConection(Session conection) {
        this.conection = conection;
    }

    public String getIdConection() {
        return idConection;
    }

    public void setIdConection(String idConection) {
        this.idConection = idConection;
    }
}
