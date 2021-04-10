package edu.pucmm.eict.Controllers;

import com.google.gson.Gson;
import edu.pucmm.eict.Entidades.Formulario;
import edu.pucmm.eict.Entidades.Usuario;
import edu.pucmm.eict.Servicios.UsuarioServices;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.common.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.ws;
import static j2html.TagCreator.*;
import static j2html.TagCreator.a;


public class RoutesController {
    //Creando el repositorio de las sesiones recibidas.
  //  public static List<Session> usuariosConectados = new ArrayList<>();
    public static List<ConnectionsController> usuariosConnected = new ArrayList<>();
    Javalin app;

    public RoutesController(Javalin app){
      this.app = app;
    };

    public void routes(){
        app.get("/", ctx -> {ctx.render("Templates/login.html");});

        app.routes( () -> {


            post("/login", ctx -> {
                Gson aux = new Gson();

                Usuario user = aux.fromJson(ctx.body(), Usuario.class);

                for (Usuario u:
                        UsuarioServices.getInstancia().findAll()) {
                    if (u.getUsuario().contentEquals(user.getUsuario())
                    && u.getPassword().contentEquals(user.getPassword())){

                        String a = aux.toJson(u, Usuario.class);
                        System.out.println("ESTO ES EL JSON: " + a);
                        ctx.result(a); //respuesta hacia el servidor
                    }else {
                        ctx.result("Error de credenciales");
                    }

                }


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
                get("/mapa", ctx -> ctx.render("/Templates/map.html"));

            });

            ws("/wsConnect", ws -> {



                ws.onConnect(ctx -> {
                    System.out.println("CONEXIÓN INICIADA CON EL CLIENTE ID:=====> "+ctx.getSessionId());

                    ConnectionsController sesion = new ConnectionsController(ctx.session, ctx.getSessionId());
                    usuariosConnected.add(sesion);
                    //usuariosConectados.add(ctx.session);

                    //Connections con = new Connections(ctx.session, ctx.getSessionId());
                    //usuariosConectados2.add(con);
                    //Se agrega el cliente a la lista se clientes conectados
                });

                ws.onMessage(ctx -> { //ESTO ES LO QUE VA A SUCEDER AL RECIBIR UN MENSAJE

                    //Puedo leer los header, parametros entre otros.
                    //PARA RECIBIR DATA DEL CLIENTE
                    ctx.headerMap();
                    ctx.pathParamMap();
                    ctx.queryParamMap();


                    Gson aux = new Gson();
                    Usuario user = new Usuario();
                    Formulario form = new Formulario();
                    System.out.println("EL CLIENTE ID: ====>"+ctx.getSessionId()+" HA ENVIADO UN MENSAJE:\n ");
                    if (ctx.message().length() < 80){
                        user = aux.fromJson(ctx.message(),Usuario.class);
                        for (Usuario u:
                                UsuarioServices.getInstancia().findAll()) {
                            if (user.getUsuario().contentEquals(u.getUsuario()) &&
                                 user.getPassword().contentEquals(u.getPassword())){
                                System.out.println("EL USUARIO ES REAL EN LA BDD");
                                //enviarMensajeAClientesConectados("Login Exitoso", ctx.getSessionId());
                                ctx.send("...");
                                System.out.println("ENVIADO AL CLIENTE");
                            }else {
                                System.out.println("ERROR DE CREDENCIALES!!");
                            }

                        }
                        System.out.println("EL MENSAJE RECIBIDO ES: =====>"+ user.getUsuario());

                    }else {
                        form = aux.fromJson(ctx.message(), Formulario.class);
                        System.out.println("EL MENSAJE RECIBIDO ES: =====>"+ form.getNombre());
                    }
                    System.out.println("\nFIN DEL MENSAJE");
                });

                ws.onClose(ctx -> { //ESTO ES LO QUE VA A SUCEDER AL CERRAR UNA SESION
                    System.out.println("CONEXIÓN CERRADA CON EL CLIENTE ID=====> "+ctx.getSessionId());
                    //usuariosConectados.remove(ctx.session);
                });

                ws.onError(ctx -> {
                    System.out.println("HA OCURRIDO UN ERROR EN EL WEBSOCKET");
                    System.out.println(ctx.error().getMessage());
                    System.out.println("/////////////////CAUSA////////////////");
                    System.out.println(ctx.error().getCause());
                });

                /**
                 * Filtro para activarse despues de la llamadas al contexto.
                 */
                app.wsAfter("/mensajeServidor", wsHandler -> {
                    System.out.println("Filtro para WS despues de la llamada al WS");
                    //ejecutar cualquier evento despues...

                });

            });

        } );





    };
    /**
     * Permite enviar un mensaje al cliente.
     * Ver uso de la librería: https://j2html.com/
     * @param mensaje
     * @param idSesion
     */
    public static void enviarMensajeAClientesConectados(String mensaje, String idSesion) throws IOException {

        for (ConnectionsController aux:
             usuariosConnected) {

            if (aux.getIdConection().contentEquals(idSesion)){
                aux.getConection().getRemote().sendString(p(mensaje).render());
                System.out.println("ENVIADO EL MENSAJE");
            }
        }
        /*
        }
        for(Session sesionConectada : usuariosConectados){
            try {

                sesionConectada.getRemote().sendString(p(mensaje).withClass(color).render());
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }


}
