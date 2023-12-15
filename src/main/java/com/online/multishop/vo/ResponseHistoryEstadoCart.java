package com.online.multishop.vo;


import com.online.multishop.modelo.*;

public class ResponseHistoryEstadoCart {

	private boolean status;
	private int code;
	private ShoppingHistoryEstado estado;
	private ErrorState error;

	

	public ResponseHistoryEstadoCart() {
		super();
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}



	public ShoppingHistoryEstado getEstado() {
		return estado;
	}



	public void setEstado(ShoppingHistoryEstado estado) {
		this.estado = estado;
	}

	
	
	
	

}
