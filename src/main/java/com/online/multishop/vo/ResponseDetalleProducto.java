package com.online.multishop.vo;

import com.online.multishop.modelo.ProductoDetalles;

public class ResponseDetalleProducto {
	
	private boolean status;
    private int code;
    private ProductoDetalles detalle;
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

   

	public ProductoDetalles getDetalle() {
		return detalle;
	}

	public void setDetalle(ProductoDetalles detalle) {
		this.detalle = detalle;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	


}
