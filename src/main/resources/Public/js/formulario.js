//abriendo el objeto para el websocket
var webSocket;
var tiempoReconectar = 5000;
var longitude;
var latitude;

$(document).ready(function (){
    console.info("INICIANDO EL JQUERY");
    conectar();
    navigator.geolocation.getCurrentPosition(sucessLocation, errorLocation, {enableHighAccuracy: true})

    $("#btnRegister").click(function (){



        var formData = {
            type: "form................",
            nombre: $("#fullName").val(),
            Sector: $("#sector").val(),
            nivelEscolar: $("#gradoEscolar").val(),
            latitud: latitude,
            longitud: longitude
        };
        console.log("ESTO ES LO ENVIADO EN JSON: " + formData)
        webSocket.send(JSON.stringify(formData));

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
        webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/wsConnect");

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