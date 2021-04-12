package edu.pucmm.eict.Servicios;

import edu.pucmm.eict.Entidades.Formulario;
import edu.pucmm.eict.Entidades.Usuario;

import java.util.IllegalFormatCodePointException;

public class Formulario_Service extends GestionDB<Formulario> {

    private static Formulario_Service instancia;

    public Formulario_Service() {super(Formulario.class);}

    public static Formulario_Service getInstancia(){

        if (instancia == null){
            instancia = new Formulario_Service();

        } return instancia;
    }

    public static boolean findByParams(String nombre, String sector, String nivelEscolar){

        boolean x = false;

        if (Formulario_Service.getInstancia().findAll() != null){

            for (Formulario f:
                    Formulario_Service.getInstancia().findAll()){

                if (f.getNombre().contentEquals(nombre) && f.getSector().contentEquals(sector)
                        && f.getNivelEscolar().contentEquals(nivelEscolar)){
                    x = true;
                }else {
                    x = false;
                }
            }

        } else {
            x = false;
        }

        return x;
    }

}
