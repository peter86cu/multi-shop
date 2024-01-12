var URL = "http://localhost:8090/";
var URLLOCAL = "http://localhost:8080/";
//var URLLOCAL = "https://tienda-online.web.elasticloud.uy/";





function cargarDatos() {
  var termino = false;
  var datos = new FormData();
  let cantidad = 0;

  if (
    sessionStorage.getItem("sessionId") == null ||
    sessionStorage.getItem("sessionId") == "null"
) {
    sessionStorage.setItem("nameUser", "Invitado");
    sessionStorage.setItem("sessionId", uuid.v1());
    sessionStorage.setItem("userId", "0000000000");
} 


  var idUsuario = sessionStorage.getItem("userId");


  datos.append("idUsuario", idUsuario);


  $.ajax({
    url: URL + "star",
    method: "POST",
    data: datos,
    chache: false,
    contentType: false,
    processData: false,
    dataType: "json",
    success: function (respuesta) {
      var response = JSON.stringify(respuesta, null, '\t');
      var datos = JSON.parse(response);
      if (datos.code == 200) {
        var tr2 = "";
        var cat = "";
        if (datos.tipoProducto.length > 0) {
          for (var i = 0; i < datos.tipoProducto.length; i++) {

            tr2 += `<a  class="dropdown-item" onclick="linkEnvioCateg('category.html','` + datos.tipoProducto[i].id_tipo_producto + ` ','` + datos.tipoProducto[i].descripcion + `')"
                     >` + datos.tipoProducto[i].descripcion + `</a>`;



          }
        }
        if (datos.todasCategorias.length > 0) {
          for (var i = 0; i < datos.todasCategorias.length; i++) {

            cat += `<a onclick="linkEnvioProductosPorCategoria('shop.html','` + datos.todasCategorias[i].id_categoria_producto + `','` + datos.todasCategorias[i].categoria + `')" class="nav-item nav-link">` + datos.todasCategorias[i].categoria + `</a>`;



          }
        } if (datos.carrito != null && idUsuario != "0000000000") {
          
          if(datos.carrito.detalle){
            for (let index = 0; index < datos.carrito.detalle.length; index++) {
              cantidad += datos.carrito.detalle[index].cantidad;
  
            }
            sessionStorage.setItem("sessionId", datos.carrito.cart.idcart);
          sessionStorage.setItem("cartIDSession", datos.carrito.cart.idcart);
          sessionStorage.setItem("cart", cantidad);
          document.getElementById('carrito-item').innerHTML = cantidad;
          }
         
          
        }else if(datos.carrito==null){
          sessionStorage.setItem("cart", 0);
          document.getElementById('carrito-item').innerHTML = cantidad;
        }
        $("#tipo-productos-item").append(tr2);
        $("#item-category").append(cat);
        //$('.loader_bg').fadeToggle();
        validarLogin2();
       //$('.loader_bg').fadeToggle();
        termino = true;
      }else{

        window.location.href = URLLOCAL + "maintenance.html"
      }

      if (termino) {
        $('.loader_bg').fadeToggle();
      }
    }

  }).fail(function (jqXHR, textStatus, errorThrown) {

    var msg = '';
    if (jqXHR.status === 0) {
      // msg = 'No connection.\n Verify Network.';
      //ERR_CONNECTION_REFUSED hits this one
      limpiarSession()
      window.location.href = URLLOCAL + "maintenance.html"
    } else if (jqXHR.status == 404) {
      window.location.href = URLLOCAL + "404.html"
    } else if (jqXHR.status == 500) {
      msg = 'Internal Server Error [500].';
    }

     
  });
  //$('.loader_bg').fadeToggle();
}



function cargarDatosLogin() {
  var termino = false;
  var datos = new FormData();
  let cantidad = 0;
  var idUsuario = sessionStorage.getItem("userId");


  datos.append("idUsuario", idUsuario);


  $.ajax({
    url: URL + "star",
    method: "POST",
    data: datos,
    chache: false,
    contentType: false,
    processData: false,
    dataType: "json",
    success: function (respuesta) {
      var response = JSON.stringify(respuesta, null, '\t');
      var datos = JSON.parse(response);
      if (datos.code == 200) {
        var tr2 = "";
        var cat = "";
        
        if (datos.carrito != null && idUsuario != "0000000000") {
          
          if(datos.carrito.detalle){
            for (let index = 0; index < datos.carrito.detalle.length; index++) {
              cantidad += datos.carrito.detalle[index].cantidad;
  
            }
            sessionStorage.setItem("sessionId", datos.carrito.cart.idcart);
          sessionStorage.setItem("cartIDSession", datos.carrito.cart.idcart);
          sessionStorage.setItem("cart", cantidad);
          document.getElementById('carrito-item').innerHTML = cantidad;
          }
         
          
        }else if(datos.carrito==null){
          sessionStorage.setItem("cart", 0);
          document.getElementById('carrito-item').innerHTML = cantidad;
        }
        validarLogin2();
       //$('.loader_bg').fadeToggle();
        termino = true;
      }

      if (termino) {
        $('.loader_bg').fadeToggle();
      }
    }

  }).fail(function (jqXHR, textStatus, errorThrown) {

    var msg = '';
    if (jqXHR.status === 0) {
      // msg = 'No connection.\n Verify Network.';
      //ERR_CONNECTION_REFUSED hits this one

      window.location.href = URLLOCAL + "maintenance.html"
    } else if (jqXHR.status == 404) {
      window.location.href = URLLOCAL + "404.html"
    } else if (jqXHR.status == 500) {
      msg = 'Internal Server Error [500].';
    }

    // limpiarSession()
  });
  //$('.loader_bg').fadeToggle();
}



function chequearCartMultiShop() {
  var termino = false;
  var datos = new FormData();

  var idUsuario = sessionStorage.getItem("userId");


  datos.append("idUsuario", idUsuario);


  $.ajax({
    url: URL + "star",
    method: "POST",
    data: datos,
    chache: false,
    contentType: false,
    processData: false,
    dataType: "json",
    success: function (respuesta) {
      var response = JSON.stringify(respuesta, null, '\t');
      var datos = JSON.parse(response);
      if (datos.code == 200) {
        var tr2 = "";
        var cat = "";

       if (datos.carrito != null) {
          let cantidad = 0;
          if(datos.carrito.detalle!=null){
            for (let index = 0; index < datos.carrito.detalle.length; index++) {
              cantidad += datos.carrito.detalle[index].cantidad;
  
            }
          }
          
         // sessionStorage.setItem("sessionId", datos.carrito.cart.idcart);
          //sessionStorage.setItem("cartIDSession", datos.carrito.cart.idcart);
          sessionStorage.setItem("cart", cantidad);
          document.getElementById('carrito-item').innerHTML = cantidad;
        }
        termino = true;
        //$('.loader_bg').fadeToggle();
      }

      if (termino) {
        //$('.loader_bg').fadeToggle();
      }
    }

  }).fail(function (jqXHR, textStatus, errorThrown) {

    var msg = '';
    if (jqXHR.status === 0) {
      // msg = 'No connection.\n Verify Network.';
      //ERR_CONNECTION_REFUSED hits this one

      window.location.href = URLLOCAL + "maintenance.html"
    } else if (jqXHR.status == 404) {
      window.location.href = URLLOCAL + "404.html"
    } else if (jqXHR.status == 500) {
      msg = 'Internal Server Error [500].';
    }

    // limpiarSession()
  });
  //$('.loader_bg').fadeToggle();
}

function validarSession(){
 if($("#sessionId").val()!=sessionStorage.getItem("sessionId")){
 limpiarSession()
 //mensajeSession("Sessión caducada. Autentiquese de nuevo")
 //window.location.href = "index";
 }

}

function mensajeSession(mensaje) {
  Swal.fire({
      text: mensaje,
      //type: 'warning',
      icon: "warning",
      showCancelButton: false,
      confirmButtonText: 'OK',
      cancelButtonText: "No",
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
  }).then((result) => {
      if (result.value) {
         return true;
      }
      return false;
  })
}

	var x = $('#search').val();

		$(".search").empty();
		$(document).ready(function() {
			//$("#addProductos").find(".marca").val("");
			//$("#addProductos input[type='checkbox']").prop('checked', false).change();
			$(".search").select2({
				//dropdownParent: $('#addProductos .modal-body'),
				theme: 'bootstrap-5',
				language: {
					inputTooShort: function() {
						return "Buscar productos.";
					},
					minimumInputLength: function() {
						return "Buscar productos.";
					},
					noResults: function() {
						categoriaTemp = 0;
						return "No hay resultado";
					},
					searching: function() {

						return "Buscando..";
					}
				},

				ajax: {
					url: URLLOCAL + "busqueda-global-products",
					method: "POST",
					dataType: 'json',
					delay: 250,
					data: function(params) {
						return {
							product: params.term
														
						};
					},
					processResults: function(data) {
						if (data == null)
							tipoProductoTemp = 0;
						return {
							results: data
						};
					},
					cache: false
				},
				minimumInputLength: 2
			}).on('change', function(e) {
				;
				linkEnvioProductosPorCategoria('products', $('.search').select2('data')[0].id, null)
				$("#search").select2('data', null);
			})
		});

	


