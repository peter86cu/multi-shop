package com.online.multishop.vo;




public class ResponseValidarPago {
	
	private boolean status;
	private int code;
	private ValidarPagoResponse pagoValido;
	private String respuesta;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public ValidarPagoResponse getPagoValido() {
		return pagoValido;
	}
	public void setPagoValido(ValidarPagoResponse pagoValido) {
		this.pagoValido = pagoValido;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
	
	

}
