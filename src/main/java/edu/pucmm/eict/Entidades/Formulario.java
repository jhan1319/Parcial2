package edu.pucmm.eict.Entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Formulario implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    private String nombre;

    private String Sector;

    private String nivelEscolar;

    private String latitud;

    private String longitud;

    public Formulario() {
    }

    public Formulario(String nombre, String sector, String nivelEscolar, String latitud, String longitud) {
        this.nombre = nombre;
        Sector = sector;
        this.nivelEscolar = nivelEscolar;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSector() {
        return Sector;
    }

    public void setSector(String sector) {
        Sector = sector;
    }

    public String getNivelEscolar() {
        return nivelEscolar;
    }

    public void setNivelEscolar(String nivelEscolar) {
        this.nivelEscolar = nivelEscolar;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
