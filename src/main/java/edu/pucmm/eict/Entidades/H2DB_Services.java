package edu.pucmm.eict.Entidades;

import edu.pucmm.eict.Servicios.GestionDB;
import org.h2.tools.Server;

import java.sql.SQLException;

public class H2DB_Services {

    private static H2DB_Services instancia;


    public H2DB_Services() {
    }


    public static H2DB_Services getInstance(){
        if(instancia == null){
            instancia = new H2DB_Services();
        }
        return instancia;
    }


    public void startDB(){

        try {
            //Modo servidor H2.
            Server.createTcpServer("-tcpPort",
                    "9092",
                    "-tcpAllowOthers",
                    "-tcpDaemon",
                    "-ifNotExists").start();
            //Abriendo el cliente web. El valor 0 representa puerto aleatorio.
            String status = Server.createWebServer("-trace", "-webPort", "0").start().getStatus();
            //
            System.out.println("Status Web: "+status);

        }catch (SQLException ex){
            System.out.println("Problema con la base de datos: "+ex.getMessage());
        }


    }

}
