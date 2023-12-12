package com.online.multishop.vo;

import com.online.multishop.modelo.MarcaProducto;

public class ResponseListaMarcasProducto {
	
	private boolean status;
    private int code;
    private MarcaProducto[] marcas;   
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
	
	

	public MarcaProducto[] getMarcas() {
		return marcas;
	}

	public void setMarcas(MarcaProducto[] marcas) {
		this.marcas = marcas;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}


}
