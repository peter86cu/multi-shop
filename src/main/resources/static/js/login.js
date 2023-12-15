//sessionStorage.setItem("nameUser", "Invitado");   || sessionStorage.getItem("nameUser") != "Invitado"



function isEqual(str1, str2) {
    return str1.toUpperCase() === str2.toUpperCase();
}

function login() {
    var mail = $("#txtEmail").val();
    var pass = $("#txtPassword").val();
    if (sessionStorage.getItem("userId") == null) {
        sessionStorage.setItem("userId",$("#userId").val() );
    }
    if (sessionStorage.getItem("userName") == null) {
        sessionStorage.setItem("userName", $("#userName").val());
    }
     if (sessionStorage.getItem("userName") == null) {
            sessionStorage.setItem("userName", $("#userName").val());
        }
    var sessionId = "";
    if (
        sessionStorage.getItem("userId") == "0000000000" /*&&
        sessionStorage.getItem("nameUser") == "Invitado"*/
    ) {
        sessionId = $("#sessionId").val();
        sessionStorage.setItem("sessionId", $("#sessionId").val());
        sessionStorage.setItem("cardId", $("#cardId").val());
        var datos = new FormData();
        datos.append("mail", mail);
        datos.append("password", pass);
        datos.append("sessionId", sessionId);
        var termino=false;
        $.ajax({
            url: URLLOCAL + "login",
            method: "POST",
            data: datos,
            chache: false,
            contentType: false,
            processData: false,
            dataType: "json",
            success: function (respuesta) {
                var response = JSON.stringify(respuesta, null, "\t");
                var datos = JSON.parse(response);
                if (datos.status) {
                    //sessionStorage.setItem("user", "Invitado");
                    sessionStorage.setItem("nameUser", datos.user.name);
                    sessionStorage.setItem("userId", datos.user.id);
                    sessionStorage.setItem("token", datos.resultado);
                    sessionStorage.setItem("cardId", $("#cardId").val());
                    $("#loginAutenticar").modal("hide");
                    mensajeOK("Bienvenido a Multi-Shop "+ datos.user.name);
                    //$('.loader_bg').fadeToggle();
                    guardarCarritoConLogin(datos.user.id);
                    termino = true;
                }else if(datos.code==500){
					  Swal.fire({
                        icon: "error",
                        text: datos.error.menssage,
                    });
                    
				} else {
                    
                    Swal.fire({
                        icon: "error",
                        text: datos.resultado,
                    });
                    if(termino){
                        $('.loader_bg').fadeToggle();
                    }
                    
                }
            },
        });
    }
}

function salir() {
    $.ajax({
        url: URLLOCAL + "logout",
        method: "GET",
        data: "",
        chache: false,
        contentType: false,
        processData: false,
        dataType: "json",
        success: function (respuesta) {
            var response = JSON.stringify(respuesta, null, "\t");
            var datos = JSON.parse(response);
            if (datos.status == 200) {
                sessionStorage.setItem("nameUser", "Invitado");
                // sessionStorage.setItem("sessionId",uuid.v1());
                sessionStorage.removeItem("token");
                sessionStorage.removeItem("cart");
                sessionStorage.setItem("userId", "0000000000");

                window.location.href = "index";
            } else if (
                datos.status == 201 &&
                sessionStorage.getItem("token") != datos.resultado
            ) {
                sessionStorage.setItem("nameUser", "Invitado");
                sessionStorage.removeItem("token");
                sessionStorage.removeItem("cart");
                sessionStorage.setItem("userId", "0000000000");
                //sessionStorage.removeItem("sessionId",uuid.v1());
                window.location.href = "index";
            }
        },
    });
}

function mensajeOK(mensaje) {
    Swal.fire({
        text: mensaje,
        //type: "success",
        icon: "success",
        showCancelButton: false,
        confirmButtonText: "OK",
        cancelButtonText: "No",
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
    }).then((result) => {
        if (result.value) {
           location.reload();
        }
    });
}



