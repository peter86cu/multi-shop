package com.online.multishop.vo;

import java.util.ArrayList;
import java.util.List;

import com.online.multishop.modelo.*;

public class ResponseCart {

	private boolean status;
	private int code;
	private CarritoDetalle cartDetalle;
	private ErrorState error;

	

	public ResponseCart() {
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

	
	
	public CarritoDetalle getCartDetalle() {
		return cartDetalle;
	}

	public void setCartDetalle(CarritoDetalle cartDetalle) {
		this.cartDetalle = cartDetalle;
	}
	
	

}
