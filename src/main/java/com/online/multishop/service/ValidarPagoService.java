package com.online.multishop.service;


import com.online.multishop.vo.*;
import com.ayalait.logguerclass.Notification;
import com.ayalait.response.ResponseResultado;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.multishop.modelo.OrdenPago;
import com.multishop.response.ResponseOrdenPago;
import com.multishop.response.ResponseValidarPago;




public interface ValidarPagoService {

	ResponseValidarPago validarPagoOrden(RequestValidarPago request)  throws JsonProcessingException;
	int obtenerNumeroOrden();
	ResponseResultado crearOrdenPago(OrdenPago orden);
	ResponseOrdenPago obtenerOrdenPagoPorUsuarios(String idusuario);
	ResponseResultado guardarLog(Notification noti);
	
    

}
