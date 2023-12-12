package com.online.multishop.vo;

import com.online.multishop.modelo.OrdenPago;

public class ResponseOrdenPago {
	
	private int code;
	private boolean status;
	private OrdenPago[] lstOrdenPago;
	private String resultado;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public OrdenPago[] getLstOrdenPago() {
		return lstOrdenPago;
	}
	public void setLstOrdenPago(OrdenPago[] lstOrdenPago) {
		this.lstOrdenPago = lstOrdenPago;
	}
	
	

}
