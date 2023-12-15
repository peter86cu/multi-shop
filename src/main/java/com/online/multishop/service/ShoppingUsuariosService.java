package com.online.multishop.service;



import org.springframework.web.client.RestTemplate;

import com.ayalait.logguerclass.Notification;
import com.online.multishop.modelo.*;
import com.online.multishop.modelo.ResponseResultado;
import com.online.multishop.vo.*;

public interface ShoppingUsuariosService {

	ResponseResultado crearUsuario(ShoppingUsuarios usuario, String token);
	ResponseResultado obtenerToken(String mail, String pwd);
	ResponseUsuario obtenerDatosUsuarioLogin(String token, String mail);
	ResponseUsuario buscarUsuarioPorId(String token, String id);
	ResponseUsuario validarUsuario(String mail, String token);
	String salir(RestTemplate template);
	ResponseResultado  cambiarPassword(String idUsuario, String pass, String token);
	
	ResponseResultado guardarDireccionUsuario(DireccionUsuario dir,String token);
	ResponseDirecciones recuperarDreccionUsuarioPorId(String idUsuario, String token);
	ResponseResultado eliminarDreccionUsuarioPorId(int id, String token);
	
	ResponseResultado guardarLog(Notification noti);
	
	

}
