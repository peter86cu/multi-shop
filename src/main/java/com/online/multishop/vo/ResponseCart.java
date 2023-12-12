package com.online.multishop.vo;

import java.util.ArrayList;
import java.util.List;

import com.online.multishop.modelo.*;


public class ResponseCart {
	
	private ShoppingCart cart;
	private List<ShoppingCartDetailTemp> detalle;
	public ShoppingCart getCart() {
		return cart;
	}
	public void setCart(ShoppingCart cart) {
		this.cart = cart;
	}
	
	public ResponseCart() {
		super();
		// TODO Auto-generated constructor stub
		//this.detalle= null;
		//this.cart= null;
	}
	public List<ShoppingCartDetailTemp> getDetalle() {
		return detalle;
	}
	public void setDetalle(List<ShoppingCartDetailTemp> detalle) {
		this.detalle = detalle;
	}
	
	

}
