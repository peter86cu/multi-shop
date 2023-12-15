package com.online.multishop.service;




import com.online.multishop.modelo.*;
import com.online.multishop.modelo.ResponseResultado;
import com.online.multishop.vo.*;
import com.ayalait.logguerclass.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;




public interface ValidarPagoService {

	ResponseValidarPago validarPagoOrden(RequestValidarPago request)  throws JsonProcessingException;
	int obtenerNumeroOrden();
	ResponseResultado crearOrdenPago(OrdenPago orden);
	ResponseOrdenPago obtenerOrdenPagoPorUsuarios(String idusuario);
	ResponseResultado guardarLog(Notification noti);
	
    

}
