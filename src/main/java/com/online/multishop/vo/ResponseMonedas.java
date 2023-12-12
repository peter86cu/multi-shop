package com.online.multishop.vo;

import com.online.multishop.modelo.Moneda;

public class ResponseMonedas {

	private boolean status;
	private int code;
	private Moneda[] monedas;
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

	public Moneda[] getMonedas() {
		return monedas;
	}

	public void setMonedas(Moneda[] monedas) {
		this.monedas = monedas;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

}
