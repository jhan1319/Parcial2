
var webSocket;
var tiempoReconectar = 5000;
//////////////////////////SCRIPT DE LEONARDO PARA EL CRUD////////////////////////////////////////////

    var elementToEdit = 0;
    window.onload = function () {
    var localStorageKeyName = 'forms';
    loadFromLocalStorage();


    function loadFromLocalStorage(formularioToEdit) {

    if (formularioToEdit != null) {
    document.getElementById("name").value = formularioToEdit.nombre
    document.getElementById("sector").value = formularioToEdit.Sector
    document.getElementById("nivel").value = formularioToEdit.nivelEscolar
    document.getElementById("latitud").value = formularioToEdit.latitud
    document.getElementById("longitud").value = formularioToEdit.longitud
}

    var formularios = [],
    dataInLocalStorage = JSON.parse(localStorage.getItem("forms")),
    gridBody = document.querySelector("#grid tbody");

    if (dataInLocalStorage !== null) {
    formularios = dataInLocalStorage;
}

    // Draw TR from TBODY
    gridBody.innerHTML = '';

    formularios.forEach(function (x, i) {
    parsed = JSON.parse(x)
    var tr = document.createElement("tr"),
    tdName = document.createElement("td"),
    tdsector = document.createElement("td"),
    tdNivel = document.createElement("td"),
    tdLatitud = document.createElement("td"),
    tdLongitud = document.createElement("td")
    tdRemove = document.createElement("td"),
    btnRemove = document.createElement("button");
    tdEdit = document.createElement("td"),
    btnEdit = document.createElement("button");
    tdSend = document.createElement("td"),
    btnSend = document.createElement("button");

    tdName.innerHTML = parsed.nombre;
    tdsector.innerHTML = parsed.Sector;
    tdNivel.innerHTML = parsed.nivelEscolar;
    tdLatitud.innerHTML = parsed.latitud;
    tdLongitud.innerHTML = parsed.longitud;



    btnRemove.textContent = 'Remove';
    btnRemove.className = 'btn btn-xs btn-danger';
    btnRemove.addEventListener('click', function(){
    removeFromLocalStorage(i);
});
    tdEdit.appendChild(btnRemove);

    btnEdit.textContent = 'Edit';
    btnEdit.className = 'btn btn-xs btn-danger';
    btnEdit.addEventListener('click', function(){
    editFromLocalStorage(i);
});

    tdEdit.appendChild(btnEdit)

    btnSend.textContent = 'Send';
    btnSend.className = 'btn btn-xs btn-danger';
    btnSend.addEventListener('click', function(){
    sendFromLocalStorage(i);
});

    tdSend.appendChild(btnSend)

    tr.appendChild(tdName);
    tr.appendChild(tdsector);
    tr.appendChild(tdNivel);
    tr.appendChild(tdLatitud);
    tr.appendChild(tdLongitud);
    tr.appendChild(tdRemove);
    tr.appendChild(tdEdit);


    gridBody.appendChild(tr);
});
}

    function removeFromLocalStorage(i){
    var formularios = [],
    dataInLocalStorage = JSON.parse(localStorage.getItem("forms"));

    formularios = dataInLocalStorage;
    formularios.splice(i, 1);
    localStorage.setItem(localStorageKeyName, JSON.stringify(formularios));

    loadFromLocalStorage();
}
    function editFromLocalStorage(i){
    var formulario = null,
    dataInLocalStorage = JSON.parse(localStorage.getItem("forms"));
    formulario = JSON.parse(dataInLocalStorage[i])
    elementToEdit = i;
    loadFromLocalStorage(formulario);
}
    function sendFromLocalStorage(i){

}
}
    function editElement(){
    var nombre = document.getElementById("name").value
    var sector = document.getElementById("sector").value
    var nivel  = document.getElementById("nivel").value
    var latitud = document.getElementById("latitud").value
    var longitud = document.getElementById("longitud").value

    var formulario = null,
    dataInLocalStorage = JSON.parse(localStorage.getItem("forms"));
    formulario = JSON.parse(dataInLocalStorage[elementToEdit])

    formulario.nombre = nombre;
    formulario.Sector = sector;
    formulario.nivelEscolar = nivel;
    formulario.latitud = latitud;
    formulario.longitud = longitud;

    dataInLocalStorage[elementToEdit] = JSON.stringify(formulario)

    localStorage.setItem("forms", JSON.stringify(dataInLocalStorage))
    window.location.reload()

}
//////////////////////////SCRIPT DE LEONARDO PARA EL CRUD////////////////////////////////////////////

$(document).ready(function () {
    console.log("INICIANDO CONEXION CON EL WS SERVER")
    conectar(); //CONECTAR AL WS SERVER EN JAVALIN AL CARGAR EL DOC.

    var fm = []

    fm.push(localStorage.getItem("forms"));

    var json = JSON.parse(fm.toString()); //Aquí está el JSON con la data para enviar

    //console.log("ESTA ES LA DATA PARA ENVIAR: "+ json)

    $("#sync").click(function (){
        console.log("ENVIANDO FORMULARIOS AL SERVIDOR...")
        json.forEach( element => webSocket.send(element)
            // webSocket.send(index);
        )
        //webSocket.send(json);
        console.log("ARCHIVOS ENVIADOS!")
        window.alert("ARCHIVOS ENVIADOS!")
        // window.location.reload()
    })


})

function conectar() {
    webSocket = new WebSocket("wss://" + location.hostname + ":" + location.port + "/wsConnect");

    //indicando los eventos:
    webSocket.onmessage = function(data){recibirInformacionServidor(data);};
    webSocket.onopen  = function(e){ console.log("Conectado - status "+this.readyState); };
    webSocket.onclose = function(e){
        console.log("Desconectado - status "+this.readyState);
    };
}

/**
 *
 * @param mensaje
 */
function recibirInformacionServidor(mensaje){
    console.log("R DEL SERVER: ===> "+mensaje.data)
    //$("#mensajeServidor").append(mensaje.data);
}

setInterval(verificarConexion, tiempoReconectar); //Web Worker para reconectar

function verificarConexion(){
    if(!webSocket || webSocket.readyState == 3){
        conectar();
    }
}