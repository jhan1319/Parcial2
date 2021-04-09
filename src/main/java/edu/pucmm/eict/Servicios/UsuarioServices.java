package edu.pucmm.eict.Servicios;

import edu.pucmm.eict.Entidades.Usuario;

public class UsuarioServices extends GestionDB<Usuario> {

    private static UsuarioServices instancia;

    public UsuarioServices() { super(Usuario.class);}

    public static UsuarioServices getInstancia(){

        if (instancia == null){
            instancia = new UsuarioServices();
        }
        return instancia;
    };



}
