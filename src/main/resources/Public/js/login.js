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

            var resp = response.data

            localStorage.setItem("loggedUser", JSON.stringify(resp)); //Aqui se guarda el usuario completo en localStorage
            console.log(JSON.stringify(resp))
            
            if (!resp.toString().localeCompare("Error de credenciales")) {
                window.location.href = '/'
            } else {
                if (resp.rol.localeCompare("admin") == 0) {
                    window.location.href = '/admin'
                } else {
                    window.location.href = '/home'
                }                
            }
            

        }, (error) =>{
            console.log("ERROR EN LA RESPUESTA: "+error);
            }
        )
    })

})
