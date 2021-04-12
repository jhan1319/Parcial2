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

    public static Usuario findUserByUsuario(String user){

        Usuario user1 = new Usuario();

        for (Usuario u:
                UsuarioServices.getInstancia().findAll()) {
            if (u.getUsuario().contentEquals(user)){

                user1 = u;
                break;
            }
        }

        return user1;

    }



}
