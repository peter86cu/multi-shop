package com.online.multishop.service;



import java.util.List;

import com.online.multishop.vo.*;
import com.ayalait.logguerclass.Notification;
import com.online.multishop.modelo.*;
import com.online.multishop.modelo.ResponseResultado;



public interface ParametrosService {

	ResponseTipoProducto listadoTipoProducto();
	
    ResponseCategorias listarCategorias();
    
    ResponseListaMarcasProducto listadoMarcasProducto();
    
    ResponseListaModeloProducto listadoModelosProducto();    
    
    ResponseListaProductos consultarListaProductos();
    
    ResponseMonedas listarMonedas();
    
    ResponseImagenesProducto imagenesProducto(String id);
    
    ResponseDetalleProducto detalleProducto(String id);
    
    ResponseResultado guardarCarrito(RequestAddCart request);
    
    ResponseCart obtenerCarrito(String idCart,String idUsuario);
    
    ResponseCartUsuario obtenerCarritoPorUsuario(String idUsuario);
    
    ResponseHistoryEstadoCart obtenerEstadoCarrito(int id);
    
    ResponseResultado guardarLog(Notification noti);

}
