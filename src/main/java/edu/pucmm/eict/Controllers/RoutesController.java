package edu.pucmm.eict.Controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import edu.pucmm.eict.Entidades.Formulario;
import edu.pucmm.eict.Entidades.Usuario;
import edu.pucmm.eict.Servicios.Converter;
import edu.pucmm.eict.Servicios.FormReceived;
import edu.pucmm.eict.Servicios.Formulario_Service;
import edu.pucmm.eict.Servicios.UsuarioServices;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.common.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.ws;
import static j2html.TagCreator.*;
import static j2html.TagCreator.a;

public class RoutesController {
    // Creando el repositorio de las sesiones recibidas.
    // public static List<Session> usuariosConectados = new ArrayList<>();
    public static List<ConnectionsController> usuariosConnected = new ArrayList<>();
    Javalin app;

    public RoutesController(Javalin app) {
        this.app = app;
    };

    public void routes() {
        app.get("/", ctx -> {



            Usuario user = ctx.sessionAttribute("loggedUser");
            if (user == null) {
                ctx.render("Templates/login.html");
            } else {
                if (user.getRol().equalsIgnoreCase("admin")) {
                    ctx.redirect("/admin");
                } else {
                    ctx.redirect("/home");
                }
            }

        });


        app.routes(() -> {

            path("/login", () -> {
                post(ctx -> {
                    Gson aux = new Gson();

                    Usuario user = aux.fromJson(ctx.body(), Usuario.class);

                    for (Usuario u : UsuarioServices.getInstancia().findAll()) {
                        if (u.getUsuario().contentEquals(user.getUsuario())
                                && u.getPassword().equals(user.getPassword())) {
                            String a = aux.toJson(u, Usuario.class);
                            System.out.println("ESTO ES EL JSON: " + a);
                            ctx.sessionAttribute("loggedUser", u);
                            ctx.result(a); // respuesta hacia el servidor
                            break;
                        } else {
                            ctx.result("Error de credenciales");
                        }

                    }
                });
            });
            path("/logout", () -> {
                get("/", ctx -> {
                    ctx.sessionAttribute("loggedUser");
                    ctx.req.getSession().removeAttribute("loggedUser");
                    ctx.redirect("/");
                });
            });

            path("/register", () -> {

                get("/", ctx -> {
                    ctx.render("Templates/user_Register.html");
                });

                post("/addUser", ctx -> {

                    String nombre = ctx.formParam("name");
                    String apellido = ctx.formParam("last_name");
                    String usuario = ctx.formParam("username");
                    String password = ctx.formParam("password");
                    String rol = ctx.formParam("rol");

                    Usuario user = new Usuario(nombre, apellido, usuario, password, rol);
                    UsuarioServices.getInstancia().crear(user);
                    ctx.redirect("/admin");
                });

            });

            path("/home", () -> {
                get("/", ctx -> ctx.render("Templates/home.html"));
                get("/encuesta", ctx -> ctx.render("Templates/formulario.html"));
                get("/mapa", ctx -> ctx.render("/Templates/map.html"));

            });
            path("/admin", () -> {
                get("/", ctx -> ctx.render("Templates/admin.html"));
                get("/encuesta", ctx -> ctx.render("Templates/formulario.html"));
                get("/mapa", ctx -> ctx.render("/Templates/map.html"));

            });

            path("encuestas", () -> {
                get("/", ctx -> {
                    ctx.render("/Templates/forms.html");
                });
            });
            path("/list-forms", () -> {
                get("/", ctx -> {
                    Map<String, Object> model = new HashMap<>();
                    List<Formulario> forms = Formulario_Service.getInstancia().findAll();
                    List<String> latitudes = new ArrayList<>();
                    List<String> longitudes = new ArrayList<>();

                    /*for (Formulario f:
                         forms) {
                        latitudes.add(f.getLatitud());
                        longitudes.add(f.getLongitud());
                    }*/



                    model.put("forms", forms);
                    ctx.render("/templates/list-forms.html", model);
                });
            });

            path("crud-users", () -> {
                get("/", ctx -> {
                    Map<String, Object> model = new HashMap<>();
                    List<Usuario> usuarios = UsuarioServices.getInstancia().findAll();
                    model.put("usuarios", usuarios);
                    ctx.render("/templates/crud-users.html", model);
                });
                get("/delete/:id", ctx -> {
                    Integer id = ctx.pathParam("id", Integer.class).get();
                    UsuarioServices.getInstancia().eliminar(id);
                    ctx.redirect("/crud-users");
                });
                get("/edit/:id", ctx -> {
                    Integer id = ctx.pathParam("id", Integer.class).get();
                    Usuario u = UsuarioServices.getInstancia().find(id);

                    Map<String, Object> model = new HashMap<>();
                    model.put("usuario", u);
                    ctx.render("/templates/edit-user.html", model);
                });
                post("/edit/", ctx -> {
                    Integer id = Integer.valueOf(ctx.formParam("id"));
                    String nombre = ctx.formParam("nombre");
                    String apellido = ctx.formParam("apellido");
                    String usuario = ctx.formParam("usuario");
                    String password = ctx.formParam("password");
                    String rol = ctx.formParam("rol");

                    Usuario u = UsuarioServices.getInstancia().find(id);

                    u.setNombre(nombre);
                    u.setApellido(apellido);
                    u.setUsuario(usuario);
                    u.setPassword(password);
                    u.setRol(rol);

                    UsuarioServices.getInstancia().editar(u);
                    ctx.redirect("/crud-users");
                });
            });

            ws("/wsConnect", ws -> {

                ws.onConnect(ctx -> {
                    System.out.println("CONEXIÓN INICIADA CON EL CLIENTE ID:=====> " + ctx.getSessionId());

                    ConnectionsController sesion = new ConnectionsController(ctx.session, ctx.getSessionId());
                    usuariosConnected.add(sesion);
                    // usuariosConectados.add(ctx.session);

                    // Connections con = new Connections(ctx.session, ctx.getSessionId());
                    // usuariosConectados2.add(con);
                    // Se agrega el cliente a la lista se clientes conectados
                });

                ws.onMessage(ctx -> { // ESTO ES LO QUE VA A SUCEDER AL RECIBIR UN MENSAJE

                    // Puedo leer los header, parametros entre otros.
                    // PARA RECIBIR DATA DEL CLIENTE
                    ctx.headerMap();
                    ctx.pathParamMap();
                    ctx.queryParamMap();

                    FormReceived data = Converter.fromJsonString(ctx.message());
                    System.out.println("ESTE ES EL MENSAJE CONVERTIDO " + data.getNombre());

                    Formulario f = new Formulario(data.getNombre(), data.getSector(), data.getNivelEscolar(),
                            data.getLatitud(), data.getLongitud(), UsuarioServices.findUserByUsuario(data.getUser()));

                    System.out.println("EL ID DEL FORMULARIO NUEVOX////////// " + f.getId());

                    if (Formulario_Service.findByParams(data.getNombre(), data.getSector(), data.getNivelEscolar())) {
                        System.out.println("////////////LOS FORMULARIOS YA EXISTEN EN LA BDD////////////");
                    } else {
                        Formulario_Service.getInstancia().crear(f);
                        System.out.println("////////////INGRESADOS LOS FORMS EN LA BDD////////////");
                    }

                    // dataList.add(aux.fromJson(ctx.message(), Formulario.class));

                    // Usuario user = new Usuario();
                    // Formulario form = new Formulario();
                    System.out.println("EL CLIENTE ID: ====>" + ctx.getSessionId() + " HA ENVIADO UN MENSAJE:\n ");

                    System.out.println("EL MENSAJE RECIBIDO ES: =====>" + ctx.message());
                    // form = aux.fromJson(ctx.message(), Formulario.class); //aquí se convierte en
                    // clase el JSON recibido
                    // System.out.println("EL MENSAJE RECIBIDO ES: =====>" + form.getNombre());
                    System.out.println("\nFIN DEL MENSAJE");
                });

                ws.onClose(ctx -> { // ESTO ES LO QUE VA A SUCEDER AL CERRAR UNA SESION
                    System.out.println("CONEXIÓN CERRADA CON EL CLIENTE ID=====> " + ctx.getSessionId());
                    // usuariosConectados.remove(ctx.session);
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
                    // ejecutar cualquier evento despues...

                });

            });

        });

    };

    /**
     * Permite enviar un mensaje al cliente. Ver uso de la librería:
     * https://j2html.com/
     * 
     * @param mensaje
     * @param idSesion
     */
    public static void enviarMensajeAClientesConectados(String mensaje, String idSesion) throws IOException {

        for (ConnectionsController aux : usuariosConnected) {

            if (aux.getIdConection().contentEquals(idSesion)) {
                aux.getConection().getRemote().sendString(p(mensaje).render());
                System.out.println("ENVIADO EL MENSAJE");
            }
        }
        /*
         * } for(Session sesionConectada : usuariosConectados){ try {
         * 
         * sesionConectada.getRemote().sendString(p(mensaje).withClass(color).render());
         * } catch (IOException e) { e.printStackTrace(); }
         */
    }

}
