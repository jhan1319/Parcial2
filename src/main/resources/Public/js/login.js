$(document).ready(function () {

    $("#inicioSesion").submit(function (e) {
        e.preventDefault()
        axios({
            method: 'post',
            url:"http://"+ location.hostname + ":" + location.port + "/login",
            data:{
                usuario: $("#txtUser").val(),
                password: $("#txtPassword").val()
            }
        }).then((response) => {

            console.log("ESTA ES LA RESPUESTA: " + response.data);



        }, (error) =>{
            console.log("ERROR EN LA RESPUESTA: "+error);
            }
        )
    })

})
