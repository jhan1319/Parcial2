//abriendo el objeto para el websocket
var webSocket;
var tiempoReconectar = 5000;
var cont = 0;
var longitude;
var latitude;

$(document).ready(function (){
    console.info("INICIANDO EL JQUERY");
    //conectar();
    navigator.geolocation.getCurrentPosition(sucessLocation, errorLocation, {enableHighAccuracy: true})

    $("#btnRegister").click(function (){

        var formData = {//ESTO ES UN JSON DEL FORMULARIO REALIZADO
           // type: "form................",
            nombre: $("#fullName").val(),
            Sector: $("#sector").val(),
            nivelEscolar: $("#gradoEscolar").val(),
            latitud: latitude,
            longitud: longitude,
            user: JSON.parse(localStorage.getItem("loggedUser"))["usuario"]
        };

        var forms = [];
        var formsInLocalStorage = JSON.parse(localStorage.getItem("forms"))
        if (formsInLocalStorage !== null) {
            forms = formsInLocalStorage
        }
        forms.push(JSON.stringify(formData))
        localStorage.setItem("forms", JSON.stringify(forms))

        var user_rol = JSON.parse(localStorage.getItem("loggedUser"))["rol"];        
        if (user_rol == "admin") {
            window.location.href = '/admin'
        } else {    
            window.location.href = '/home'
        } 

        
        //webSocket.send(JSON.stringify(formData));

    });

    /**
     *
     * @param mensaje
     */
    function recibirInformacionServidor(mensaje){
        console.log("RECIBIENDO LA SIGUIENTE DATA DESDE EL SERVER: "+mensaje.data)
        $("#mensajeServer").append(mensaje.data);
    }

    /**
     *
     * @param mensaje
     */
    function recibirInformacionServidor(mensaje){
        console.log("R DEL SERVER: ===> "+mensaje.data)
        //$("#mensajeServidor").append(mensaje.data);
    }

    function conectar() {
        webSocket = new WebSocket("wss://" + location.hostname + "/wsConnect");
	console.log(location.hostname + ":" + location.port)
        //indicando los eventos:
        webSocket.onmessage = function(data){recibirInformacionServidor(data);};
        webSocket.onopen  = function(e){ console.log("Conectado - status "+this.readyState); };
        webSocket.onclose = function(e){
            console.log("Desconectado - status "+this.readyState);
        };
    }

    setInterval(verificarConexion, tiempoReconectar); //para reconectar.

    function sucessLocation(position){
        console.log(position)
         longitude = position.coords.longitude;
         latitude = position.coords.latitude

    }
    function verificarConexion(){
        if(!webSocket || webSocket.readyState == 3){
            conectar();
        }
    }

    function errorLocation(){
        console.log("Error al obtener la localización")
    }

});
