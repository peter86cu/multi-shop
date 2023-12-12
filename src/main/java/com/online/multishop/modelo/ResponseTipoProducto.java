package com.online.multishop.modelo;



public class ResponseTipoProducto {

	private boolean status;
	private int code;
	private TipoProducto[] tipoProductos;
	private String resultado;

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

	public TipoProducto[] getTipoProductos() {
		return tipoProductos;
	}

	public void setTipoProductos(TipoProducto[] tipoProductos) {
		this.tipoProductos = tipoProductos;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

}
