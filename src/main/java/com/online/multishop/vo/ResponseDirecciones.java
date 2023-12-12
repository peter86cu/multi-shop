package com.online.multishop.vo;

import com.online.multishop.modelo.*;

public class ResponseDirecciones {
	
	private int code;
	private boolean status;
	private String resultado;
	private DireccionUsuario[] direcciones;


	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public DireccionUsuario[] getDirecciones() {
		return direcciones;
	}
	public void setDirecciones(DireccionUsuario[] direcciones) {
		this.direcciones = direcciones;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	

}
