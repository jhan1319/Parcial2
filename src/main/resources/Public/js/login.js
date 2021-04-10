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

            console.log(response.data)

            localStorage.setItem("loggedUser", JSON.stringify(response.data)); //Aqui se guarda el usuario completo en localStorage

            window.location.href = '/home'

        }, (error) =>{
            console.log("ERROR EN LA RESPUESTA: "+error);
            }
        )
    })

})
