package com.online.multishop.vo;

import java.util.List;

import com.online.multishop.modelo.*;

public class ResponseDirecciones {
	
	private int code;
	private boolean status;
	private String resultado;
	private List<DireccionUsuario>  direcciones;
	private ErrorState error;


	
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
	
	public List<DireccionUsuario> getDirecciones() {
		return direcciones;
	}
	public void setDirecciones(List<DireccionUsuario> direcciones) {
		this.direcciones = direcciones;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public ErrorState getError() {
		return error;
	}
	public void setError(ErrorState error) {
		this.error = error;
	}
	
	

}
