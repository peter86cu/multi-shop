package com.online.multishop.service;



import java.util.List;

import com.online.multishop.vo.*;
import com.online.multishop.modelo.*;
import com.online.multishop.modelo.ResponseResultado;



public interface ParametrosService {

	ResponseTipoProducto listadoTipoProducto();
	
    ResponseCategorias listarCategorias();
    
    ResponseListaMarcasProducto listadoMarcasProducto();
    
    ResponseListaModeloProducto listadoModelosProducto();    
    
    ResponseListaProductos consultarListaProductos();
    
    ResponseMonedas listarMonedas();
    
    ProductoImagenes[] imagenesProducto(String id);
    
    ResponseDetalleProducto detalleProducto(String id);
    
    ResponseResultado guardarCarrito(RequestAddCart request);
    
    ResponseCart obtenerCarrito(String idCart,String idUsuario);
    
    ResponseCart[] obtenerCarritoPorUsuario(String idUsuario);
    
    ShoppingHistoryEstado obtenerEstadoCarrito(int id);

}
