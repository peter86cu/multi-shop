package com.online.multishop.vo;

import java.util.List;

import com.online.multishop.modelo.MarcaProducto;

public class ResponseListaMarcasProducto {
	
	private boolean status;
    private int code;
    private List<MarcaProducto> marcas;   
    private String resultado;
    private ErrorState error;

   

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
	
	

	public List<MarcaProducto> getMarcas() {
		return marcas;
	}

	public void setMarcas(List<MarcaProducto> marcas) {
		this.marcas = marcas;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public ErrorState getError() {
		return error;
	}

	public void setError(ErrorState error) {
		this.error = error;
	}


}
