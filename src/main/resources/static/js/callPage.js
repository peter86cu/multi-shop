function linkEnvioCateg(direccion, id, producto) {
  // sessionStorage.setItem("user", "invitado1");

  var data = sessionStorage.getItem("user");

  window.location.href = URLLOCAL + direccion + "?id=" + id;
}

function linkEnvioProductosPorCategoria(direccion, id, categoria) {
  window.location.href = URLLOCAL + direccion + "?id=" + id;
}

function linkEnvioDetalleProducto(direccion, id, categoria) {
  window.location.href = URLLOCAL + direccion + "?id=" + id;
}

function linkEnvioCardProductos(direccion) {
  if (
     $("#cardItems").val() == "0" || $("#cardItems").val() == ""
  ) {
    Swal.fire({
      position: "top-end",
      icon: "success",
      text: "datos.resultado",
      showConfirmButton: false,
      timer: 15000,
    });
  } else if (
    getQueryVariable("filter") == "detail"
  ) {
   window.location.href =
      URLLOCAL +
        "search?idCart=" +
      $("#cardId").val()+"&idUsuario="+sessionStorage.getItem("userId");

   } else if( $("#cardItems").val() != "0"){
    window.location.href =
         URLLOCAL +
           "search?idCart=" +
         $("#cardId").val()+"&idUsuario="+sessionStorage.getItem("userId");
   }
  }

function linkListadoComprasUser(direccion, id) {
  window.location.href = URLLOCAL + direccion + "?iduser=" + id;
}


function mensajeNoOK(mensaje) {
  Swal.fire({
    title: mensaje,
    //type: "warning",
    icon: "warning",
    showCancelButton: false,
    confirmButtonText: "OK",
    cancelButtonText: "No",
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
  }).then((result) => {
    if (result.value) {
      //location.reload();
    }
    return false;
  });
}

function mensajeInicio(mensaje) {
  Swal.fire({
    text: mensaje,
   // type: "warning",
    icon: "warning",
    showCancelButton: false,
    confirmButtonText: "OK",
    cancelButtonText: "No",
    confirmButtonColor: "#3085d6",
    cancelButtonColor: "#d33",
  }).then((result) => {
    if (result.value) {
      location.href = "index.html";
    }
    return false;
  });
}
