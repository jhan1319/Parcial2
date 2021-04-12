package edu.pucmm.eict.Servicios;
import com.fasterxml.jackson.annotation.*;

import java.text.Normalizer;

public class FormReceived {
    private String nombre;
    private String sector;
    private String nivelEscolar;
    private String latitud;
    private String longitud;
    private String user;
    @JsonProperty("nombre")
    public String getNombre() { return nombre; }
    @JsonProperty("nombre")
    public void setNombre(String value) { this.nombre = value; }

    @JsonProperty("Sector")
    public String getSector() { return sector; }
    @JsonProperty("Sector")
    public void setSector(String value) { this.sector = value; }

    @JsonProperty("nivelEscolar")
    public String getNivelEscolar() { return nivelEscolar; }
    @JsonProperty("nivelEscolar")
    public void setNivelEscolar(String value) { this.nivelEscolar = value; }

    @JsonProperty("latitud")
    public String getLatitud() { return latitud; }
    @JsonProperty("latitud")
    public void setLatitud(String value) { this.latitud = value; }

    @JsonProperty("longitud")
    public String getLongitud() { return longitud; }
    @JsonProperty("longitud")
    public void setLongitud(String value) { this.longitud = value; }

    @JsonProperty("user")
    public String getUser() { return user; }
    @JsonProperty("user")
    public void setUser(String value) { this.user = value; }
}
