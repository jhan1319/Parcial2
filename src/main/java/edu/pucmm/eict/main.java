package edu.pucmm.eict;

import edu.pucmm.eict.Controllers.RoutesController;
import edu.pucmm.eict.Entidades.H2DB_Services;
import edu.pucmm.eict.Entidades.Usuario;
import edu.pucmm.eict.Servicios.UsuarioServices;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

public class main {

    public static void main(String[] args) {

        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.addStaticFiles("/Public");
        }).start(7000);

        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");

        new RoutesController(app).routes();
        H2DB_Services.getInstance().startDB();
        // UsuarioServices.getInstancia().getEntityManager();
        Usuario user = new Usuario("administrador", "Apellido_Admin", "admin", "admin", "admin");
        UsuarioServices.getInstancia().crear(user);


        app.after(ctx -> {
            //System.out.println("Enviando el header de seguridad para el Service Worker");
            ctx.header("Service-Worker-Allowed", "/");
        });
    }

}
