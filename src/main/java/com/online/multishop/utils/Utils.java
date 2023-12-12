package com.online.multishop.utils;

import com.online.multishop.modelo.ProductoImagenes;
import com.online.multishop.service.ParametrosServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.UUID;



public class Utils {
	
	public static void descargarImagenProducto(ProductoImagenes producto ) throws IOException {
		
		if(producto!=null) {
			File file = new File(ParametrosServiceImpl.rutaDowloadProducto +producto.getNombre());
			if(!file.exists()) {
				byte trans[] = Base64.getDecoder().decode(producto.getImagen());
				Files.write(Paths.get(ParametrosServiceImpl.rutaDowloadProducto +producto.getNombre()), trans);
			}		
		}
		
	}
	
	public static String generarId() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	public static String rellenarConCeros(String cadena, int numCeros) {
	        String ceros = "OP-";

	        for (int i = cadena.length(); i < numCeros; i++) {
	            ceros += "0";
	        }

	        return ceros + cadena;
	    }

	public static String obtenerFechaPorFormato(String formato) {
		Date fecha = new Date(Calendar.getInstance().getTimeInMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(formato);
		return formatter.format(fecha);
	}
}
