package edu.pucmm.eict.Controllers;

import edu.pucmm.eict.Entidades.Usuario;
import edu.pucmm.eict.Servicios.UsuarioServices;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.get;


public class RoutesController {

    Javalin app;

    public RoutesController(Javalin app){
      this.app = app;
    };

    public void routes(){
        app.get("/", ctx -> {ctx.render("Templates/login.html");});
        app.routes( () -> {


            post("/login", ctx -> {




            });


            path("/register", () -> {

                get("/", ctx -> {ctx.render("Templates/user_Register.html");});

                post("/addUser", ctx -> {

                    String nombre = ctx.formParam("name");
                    String apellido = ctx.formParam("last_name");
                    String usuario = ctx.formParam("username");
                    String password = ctx.formParam("rol");
                    String rol = ctx.formParam("password");

                    Usuario user = new Usuario(nombre, apellido, usuario, password, rol);

                    UsuarioServices.getInstancia().crear(user);

                });

            });

            path("/home", () -> {

                get("/", ctx -> ctx.render("Templates/home.html"));

                get("/encuesta", ctx -> ctx.render("Templates/formulario.html"));


            });

        } );





    };
}
