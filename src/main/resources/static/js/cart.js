
function guardarCarritoConLogin(idUsuario) {
	activarLoader()
let carrito=document.getElementById("carrito-item").innerText;
if(carrito>0){
 var datos = new FormData();
    datos.append("idCart", $("#sessionId").val());
    datos.append("idUsuario", idUsuario);
    datos.append("idProducto", "");
    datos.append("precio", 0);
    datos.append("cantidad", 0);
    datos.append("accion", "login");
    datos.append("img", "");
    datos.append("producto", "");

    $.ajax({
        url: URLLOCAL + "add-item-cart",
        method: "POST",
        data: datos,
        chache: false,
        contentType: false,
        processData: false,
        dataType: "json",
        success: function (respuesta) {
			desactivarLoading()
            var response = JSON.stringify(respuesta, null, '\t');
            var datos = JSON.parse(response);
            if (datos.code == 200) {
                datos.append("idCart", datos.datosCart.cart.idcart);
                const element = 0;
                for (let index = 0; index < datos.datosCart.detalle.length; index++) {
                    element += datos.datosCart.detalle[index].cantidad;

                }
                sessionStorage.setItem("cart", element);
            }
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {

        var msg = '';
        if (jqXHR.status === 0) {
            // msg = 'No connection.\n Verify Network.';
            //ERR_CONNECTION_REFUSED hits this one

            window.location.href = URLLOCAL + "maintenance"
        } else if (jqXHR.status == 404) {
            window.location.href = URLLOCAL + "404.html"
        } else if (jqXHR.status == 500) {
            msg = 'Internal Server Error [500].';
        }

    });

}
//Buscar Carrito guardado si tiene

}

function totalPago(){
let subTotal = document.getElementById("subtotal-cart").innerText;
let iva = document.getElementById("iva-cart").innerText;
let total=   Math.floor(subTotal.slice(1)) + Math.floor(iva.slice(1));
var simbolo = document.getElementById("total-cart").innerText;
   document.getElementById('total-cart').innerHTML = simbolo +" "+total;
}

function agregarProductoCarrito(idProducto, precio, accion) {
activarLoader()
    //sessionStorage.removeItem("cart");
    let cantCart = 0;
    let total = 0;
    let cantNuevo = 0;
    var algo = 0


    if (accion == "op1") {
        cantNuevo = document.getElementById("cantidad-product-car").value;
    } else if (accion == "op2") {
        cantNuevo = document.getElementById(idProducto).value;
    } else {
        cantNuevo = 1;
    }

    /*if (sessionStorage.getItem("cart") != null) {
        cantCart = sessionStorage.getItem("cart");
    }*/


    //Sumamos mas
    total = Math.floor(cantNuevo) + Math.floor(cantCart);


    var img = "";
    document.getElementById('carrito-item').innerHTML = total;
    var cantidadProd = 0;
    sessionStorage.setItem("cart", total);
    if(sessionStorage.getItem("cardId") == null || sessionStorage.getItem("cardId") == ""){
    sessionStorage.setItem("cardId", $("#cardId").val());
    }
    if (accion != "op3") {
        cantidadProd = document.getElementById("cantidad-product-car").value;
        img = document.getElementById("img-producto0");
        
    } else {
        cantidadProd = 1;
        img = document.getElementById("img-producto");
    }

    var nameProduct = document.getElementById("name-product").innerText;
    var rImg = img.title;

    //Envio datos al WS
    var datos = new FormData();
    var idUsuario = "";
    if (sessionStorage.getItem("token") != null) {
        idUsuario = sessionStorage.getItem("userId");
    } else {
        idUsuario = "0000000000"
    }
    datos.append("idCart", $("#cardId").val());
    datos.append("idUsuario", idUsuario);
    datos.append("idProducto", idProducto);
    datos.append("precio", precio);
    datos.append("cantidad", cantidadProd);
    datos.append("accion", "insertar");
    datos.append("img", rImg);
    datos.append("producto", nameProduct);

    $.ajax({
        url: URLLOCAL + "add-item-cart",
        method: "POST",
        data: datos,
        chache: false,
        contentType: false,
        processData: false,
        dataType: "json",
        success: function (respuesta) {
			desactivarLoading()
            var response = JSON.stringify(respuesta, null, '\t');
            var datos = JSON.parse(response);
            if (datos.code == 200) {
                mensajeProduct("Producto agregado al carrito");

            }
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {

        var msg = '';
        if (jqXHR.status === 0) {
            // msg = 'No connection.\n Verify Network.';
            //ERR_CONNECTION_REFUSED hits this one

            window.location.href = URLLOCAL + "maintenance"
        } else if (jqXHR.status == 404) {
            window.location.href = URLLOCAL + "404.html"
        } else if (jqXHR.status == 500) {
            msg = 'Internal Server Error [500].';
        }
    });
    //$("#cantidad-product-car").val(1);
}


function eliminarProductoCarrito(idProducto) {
activarLoader()
    var idUsuario = "";
    //if (sessionStorage.getItem("token") != null) {
    idUsuario = sessionStorage.getItem("userId");
    // } else {
    //  idUsuario = "0000000000"
    //}
    var datos = new FormData();
    datos.append("idProducto", idProducto);
    datos.append("idUsuario", idUsuario);
    datos.append("idCart", sessionStorage.getItem("cartIDSession"));

    $.ajax({
        url: URLLOCAL + "delete-product-cart",
        method: "POST",
        data: datos,
        chache: false,
        contentType: false,
        processData: false,
        dataType: "json",
        success: function (respuesta) {
			desactivarLoading()
            var response = JSON.stringify(respuesta, null, '\t');
            var datos = JSON.parse(response);
            if (datos.code == 200) {
                mensajeOK("OK");
            }
        }
    })


}


function validarPago(direccion) {
activarLoader()
    var totalPagoText = document.getElementById("total-cart").innerText;
    var codeMoneda = document.getElementById("moneda-pago-select");
    let totalPago= totalPagoText.slice(1);
    totalPago= totalPago.trim();
    if (sessionStorage.getItem("userId") == "0000000000") {
        /* if(totalPago>0){       
             window.location.href = URLLOCAL + "checkout02.html?filter=checkout02&curenty=" + codeMoneda.value+ "&pay=" +totalPago +"&id=" + sessionStorage.getItem("cartIDSession");
         }*/
        mensajeLoginUserPago("Debe iniciar sessión para continuar.")
    } else {
        if (totalPago > 0) {
            window.location.href = URLLOCAL + "checkout01?curenty=" + codeMoneda.value + "&pay=" + totalPago + "&userId=" + $("#userId").val();
        }
    }
desactivarLoading()

}



function mensajeLoginUserPago(mensaje) {
    Swal.fire({
        text: mensaje,
        icon: 'warning',
        // icon: "warning",
        showCancelButton: true,
        confirmButtonText: 'OK',
        cancelButtonText: "No",
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
    }).then((result) => {
        if (result.value) {
            abrirModal('loginAutenticar');
        }
        return false;
    })
}
