
function isEqual(str1, str2) {
	return str1 === str2;
}

function login() {
	activarLoader()
	var mail = $("#emailInput").val();
	var pass = $("#passwordInput").val();
	if (sessionStorage.getItem("userId") == null) {
		sessionStorage.setItem("userId", $("#userId").val());
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
 		$.ajax({
			url: URLLOCAL + "login",
			method: "POST",
			data: datos,
			chache: false,
			contentType: false,
			processData: false,
			dataType: "json",
			success: function(respuesta) {
				desactivarLoading()
				var response = JSON.stringify(respuesta, null, "\t");
				var datos = JSON.parse(response);
				if (datos.status) {
					cerrarModal('loginAutenticar')
					//sessionStorage.setItem("user", "Invitado");
					sessionStorage.setItem("nameUser", datos.user.name);
					sessionStorage.setItem("userId", datos.user.id);
					sessionStorage.setItem("token", datos.resultado);
					sessionStorage.setItem("cardId", $("#cardId").val());
					//$("#loginAutenticar").modal("hide");
					//mensajeOK("Bienvenido a Multi-Shop " + datos.user.name);
					//$('.loader_bg').fadeToggle();
					location.reload();
					guardarCarritoConLogin(datos.user.id);
					
				} else  {
					Swal.fire({
						icon: "error",
						text: datos.error.menssage,
					});

				} /*else {

					Swal.fire({
						icon: "error",
						text: datos.resultado,
					});
					if (termino) {
						$('.loader_bg').fadeToggle();
					}

				}*/
			},
		});
	}
}

function salir() {
	$.ajax({
		url: URLLOCAL + "logout",
		method: "POST",
		data: "",
		chache: false,
		contentType: false,
		processData: false,
		dataType: "json",
		success: function(respuesta) {
			var response = JSON.stringify(respuesta, null, "\t");
			var datos = JSON.parse(response);
			if (datos.code == 200) {
				sessionStorage.setItem("nameUser", "Invitado");
				// sessionStorage.setItem("sessionId",uuid.v1());
				sessionStorage.removeItem("token");
				sessionStorage.removeItem("cart");
				sessionStorage.setItem("userId", "0000000000");

				window.location.href = "index";
			} else if (
				datos.code == 201 &&
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

function mensajeDeleteAcount(mensaje) {
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
			salir()
		}
	});
}



function validarPassword(event, objeto,accion) {
	const input = document.getElementById(objeto);
	input.addEventListener("change", valPasswords(objeto,accion));

	/*$(document).ready(function() {
		$(document).on('change', '#' + objeto, function() {
			if ($(this).val().trim() != "") {
				if(isEqual(pass,$(this).val().trim())){
				$("#" + objeto).removeClass("is-invalid");
				$('#password').removeClass('is-invalid');
				}else{
				$('#' + objeto).addClass('is-invalid');
				$('#password').addClass('is-invalid');
				}
			} else {
				$('#' + objeto).addClass('is-invalid');
				$('#password').addClass('is-invalid');
			}
			if(validarCampos())
			$('#btnRegistrar').prop('disabled', false);
		});
	})*/
}

function valPasswords(objeto,accion) {
	var pass = $('#password').val();

	if ($('#' + objeto).val().trim() != "") {
		if (isEqual(pass, $('#' + objeto).val().trim())) {
			$("#" + objeto).removeClass("is-invalid");
			$('#password').removeClass('is-invalid');
		} else {
			$('#' + objeto).addClass('is-invalid');
			$('#password').addClass('is-invalid');
		}
	} else {
		$('#' + objeto).addClass('is-invalid');
		$('#password').addClass('is-invalid');
	}
	if (validarCampos() && accion=="registrar")
		$('#btnRegistrar').prop('disabled', false);
	if(validarCamposUpdate() && accion=="actualizar")
		$('#btnRegistrar').prop('disabled', false);
}

function regitrarUsuario() {
	var nombre = $('#nombre').val();
	var apellidos = $("#apellidos").val();
	var email = $("#email").val()
	var telefono = $("#telefono").val()
	var tipodoc = $("#tipo-doc-select").val()
	var documento = $("#documento").val()
	var password = $("#password").val()

	var datos = new FormData();
	datos.append("nombre", nombre);
	datos.append("apellidos", apellidos);
	datos.append("email", email);
	datos.append("telefono", telefono);
	datos.append("tipodoc", tipodoc);
	datos.append("documento", documento);
	datos.append("password", password);
	$.ajax({
		url: URLLOCAL + "add-new-user",
		method: "POST",
		data: datos,
		chache: false,
		contentType: false,
		processData: false,
		dataType: "json",
		success: function(respuesta) {
			var response = JSON.stringify(respuesta, null, '\t');
			var data = JSON.parse(response);
			if (data.status) {
				mensajeOK(data.resultado);
			}else{
				mensajeErrorGenerico(data.error.menssage);
			}
		}
	})
}

function actualizarUsuario() {
	var nombre = $('#nombre').val();	
	var email = $("#email").val()
	var telefono = $("#telefono").val()	
	var password = $("#password").val()
    var id = $("#idUser").val()  
  
    
	var datos = new FormData();
	datos.append("id", id);
	datos.append("nombre", nombre);
	datos.append("email", email);
	datos.append("telefono", telefono);
	datos.append("password", password);
	$.ajax({
		url: URLLOCAL + "update-user",
		method: "POST",
		data: datos,
		chache: false,
		contentType: false,
		processData: false,
		dataType: "json",
		success: function(respuesta) {
			var response = JSON.stringify(respuesta, null, '\t');
			var data = JSON.parse(response);
			if (data.status) {
				mensajeOK(data.resultado);
			}else{
				mensajeErrorGenerico(data.error.menssage);
			}
		}
	})
}


function validarChange(event, objeto) {

	const input = document.getElementById(objeto);

	input.addEventListener("change", updateValue(objeto));


	/*$(input).ready(function() {
		$(input).on('change', '#' + objeto, function() {
			if ($(this).val().trim() == "") {
				$('#' + objeto).addClass('is-invalid');
			} else {
				$("#" + objeto).removeClass("is-invalid");
			}
			if(validarCampos())
			$('#btnRegistrar').prop('disabled', false);
		});
	})*/
}

function updateValue(objeto) {
	if ($('#' + objeto).val().trim() == "") {
		$('#' + objeto).addClass('is-invalid');
	} else {
		$("#" + objeto).removeClass("is-invalid");
	}
	if (validarCampos())
		$('#btnRegistrar').prop('disabled', false);
}

function validarCampos() {
	var validar = true;

	if ($("#nombre").val() == "") {
		//$('#nombre').addClass('is-invalid');
		validar = false;
	} if ($("#apellidos").val() == "") {
		//$('#apellidos').addClass('is-invalid');
		validar = false;
	} if ($("#email").val() == "") {
		//$('#email').addClass('is-invalid');
		validar = false;
	} if ($("#telefono").val() == "") {
		//$('#telefono').addClass('is-invalid');
		validar = false;
	} if ($("#tipo-doc-select").val() == null) {
		//$('#tipo-doc-select').addClass('is-invalid');
		validar = false;
	} if ($("#documento").val() == "") {
		//$('#documento').addClass('is-invalid');
		validar = false;
	} if ($("#password").val() == "") {
		//$('#password').addClass('is-invalid');
		validar = false;
	} if ($("#passwordConfir").val() == "") {
		//$('#passwordConf').addClass('is-invalid');
		validar = false;
	} if (!isEqual($("#password").val(), $("#passwordConfir").val())) {
		validar = false;
	}


	return validar;

}

function validarCamposUpdate() {
	var validar = true;

	if ($("#nombre").val() == "") {
		//$('#nombre').addClass('is-invalid');
		validar = false;	
	} if ($("#email").val() == "") {
		//$('#email').addClass('is-invalid');
		validar = false;
	} if ($("#telefono").val() == "") {
		//$('#telefono').addClass('is-invalid');
	
	} if ($("#password").val() == "") {
		//$('#password').addClass('is-invalid');
		validar = false;
	} if ($("#passwordConfir").val() == "") {
		//$('#passwordConf').addClass('is-invalid');
		validar = false;
	} if (!isEqual($("#password").val(), $("#passwordConfir").val())) {
		validar = false;
	}


	return validar;

}

function deleteAcount() {
	var id = $("#idUser").val()  
  
    
	var datos = new FormData();
	datos.append("idUsuario", id);
	$.ajax({
		url: URLLOCAL + "delete-acount",
		method: "POST",
		data: datos,
		chache: false,
		contentType: false,
		processData: false,
		dataType: "json",
		success: function(respuesta) {
			var response = JSON.stringify(respuesta, null, "\t");
			var data = JSON.parse(response);
			
			if (data.status) {
				mensajeDeleteAcount(data.resultado)
			}else{
				mensajeErrorGenerico(data.error.menssage);
			}
		},
	});
}

