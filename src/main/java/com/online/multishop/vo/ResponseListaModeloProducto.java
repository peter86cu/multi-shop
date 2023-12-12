package com.online.multishop.vo;

import com.online.multishop.modelo.ModeloProducto;

public class ResponseListaModeloProducto {
	
	private boolean status;
    private int code;
    private ModeloProducto[] modelo;   
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
	
	

	public ModeloProducto[] getModelo() {
		return modelo;
	}

	public void setModelo(ModeloProducto[] modelo) {
		this.modelo = modelo;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}


}
