/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.online.multishop.vo;

import com.online.multishop.modelo.*;

/**
 *
 * @author pedro
 */
public class ResponseUsuario {

    private boolean status;
    private int code;
    private ShoppingUsuarios user;
    private String resultado;



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
    

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

	public ShoppingUsuarios getUser() {
		return user;
	}

	public void setUser(ShoppingUsuarios user) {
		this.user = user;
	}

   
    

}