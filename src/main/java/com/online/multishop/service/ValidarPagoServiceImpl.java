package com.online.multishop.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

 import com.online.multishop.vo.*;

import com.ayalait.fecha.FormatearFechas;
import com.ayalait.logguerclass.Notification;
import com.ayalait.response.ResponseResultado;
import com.ayalait.utils.ErrorState;
import com.ayalait.utils.MessageCodeImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.multishop.modelo.OrdenPago;
import com.multishop.response.*;
 
@Service
public class ValidarPagoServiceImpl implements ValidarPagoService {

	public static String rutaDowloadProducto;

	private String hostStock;
	private String dlocalGo;
	private String autentication;
	public static String notificationURL;
	public static String success_pago_url;
	ObjectWriter ow = (new ObjectMapper()).writer().withDefaultPrettyPrinter();

	@Autowired
	RestTemplate restTemplate;
	
	private boolean desarrollo = false;

	void cargarServer() throws IOException {
		Properties p = new Properties();

		try {
			URL url = this.getClass().getClassLoader().getResource("application.properties");
			if (url == null) {
				throw new IllegalArgumentException("application.properties" + " is not found 1");
			} else {
				InputStream propertiesStream = url.openStream();
				p.load(propertiesStream);
				propertiesStream.close();
				this.hostStock = p.getProperty("server.stock");
				this.dlocalGo = p.getProperty("server.dlocalgo");
				this.autentication = p.getProperty("server.token");
				this.notificationURL = p.getProperty("server.notificaciones");
				this.success_pago_url = p.getProperty("server.success_pago_url");

			}
		} catch (FileNotFoundException var3) {
			System.err.println(var3.getMessage());
		}

	}

	public ValidarPagoServiceImpl() {
		try {
			if (desarrollo) {
				rutaDowloadProducto = "C:\\xampp\\htdocs\\multishop\\img\\";
				hostStock = "http://localhost:8082";
				dlocalGo = "https://api-sbx.dlocalgo.com/v1/payments/";
				autentication = "ICxxdYAWmYGMxBqBHYxwvEJotcExWHUZ:qKRLqfsYE1LZHS2PvPFPjZM5XUY8HT5Aj11UHUAD";
				notificationURL = "https://ayalait.com/notification/";
				success_pago_url = "http://localhost:8080/shopping?iduser=";
			} else {
				cargarServer();
			}
		} catch (IOException var2) {
			System.err.println(var2.getMessage());
		}

	}

	@Override
	public ResponseValidarPago validarPagoOrden(RequestValidarPago request) throws JsonProcessingException {
		
		
		ResponseValidarPago responseOrder = new ResponseValidarPago();
		
		
		 Notification noti= new Notification();


			try {

				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + autentication);
				HttpEntity<RequestValidarPago> requestEntity = new HttpEntity<>(request, headers);
				noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
				noti.setClass_id("multishop-APP");
				noti.setRequest(ow.writeValueAsString(requestEntity));
				noti.setAccion("validarPagoOrden");	
				noti.setId(UUID.randomUUID().toString());
				
				ResponseEntity<ValidarPagoResponse> response = restTemplate.exchange(this.dlocalGo , HttpMethod.POST,
						requestEntity, ValidarPagoResponse.class);

				if (response.getStatusCodeValue() == 200) {
					responseOrder.setStatus(true);
					responseOrder.setPagoValido(response.getBody());
					noti.setResponse(ow.writeValueAsString(responseOrder));			
				}

			} catch (org.springframework.web.client.HttpClientErrorException e) {
				JsonParser jsonParser = new JsonParser();
				int in = e.getLocalizedMessage().indexOf("{");
				int in2 = e.getLocalizedMessage().indexOf("}");
				String cadena = e.getMessage().substring(in, in2+1);
				JsonObject myJson = (JsonObject) jsonParser.parse(cadena);
				responseOrder.setCode(myJson.get("code").getAsInt());
				ErrorState data = new ErrorState();
				data.setCode(myJson.get("code").getAsInt());
				data.setMenssage(MessageCodeImpl.getMensajeAPIPago(myJson.get("code").getAsString() ));
				responseOrder.setCode(data.getCode());
				responseOrder.setError(data);
				try {
					noti.setResponse(ow.writeValueAsString(responseOrder));
				} catch (JsonProcessingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception ex) {
				System.err.println(ex.getLocalizedMessage());
			}
			noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			ResponseResultado result=guardarLog(noti);
			if(!result.isStatus()) {
				System.err.println(result.getError().getCode() +" "+ result.getError().getMenssage());
			}
			return responseOrder;
			
			

		
	}

	@Override
	public int obtenerNumeroOrden() {
		Notification noti= new Notification();
		int responseVal = 0;		
		noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		noti.setClass_id("multishop-APP");
		noti.setAccion("obtenerNumeroOrden");	
		noti.setId(UUID.randomUUID().toString());
		String url= this.hostStock + "/shopping/orden-number";
		URI uri= null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseEntity<Integer> response = restTemplate.exchange(uri, HttpMethod.GET,
				null, Integer.class);

		if (response.getStatusCodeValue() == 200) {
			return responseVal=response.getBody();			
		}

	 
	noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
	ResponseResultado result= guardarLog(noti);
	if(!result.isStatus()) {
		System.err.println(result.getError().getCode() +" "+ result.getError().getMenssage());
	}
	return responseVal;
	
		
	}

	@Override
	public ResponseResultado crearOrdenPago(OrdenPago orden) {
		
		ResponseResultado responseResult = new ResponseResultado();

		Notification noti = new Notification();

		try {
			String url = this.hostStock + "/shopping/orden/crear";		
			HttpHeaders headers = new HttpHeaders();
			URI uri = new URI(url);
			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");
			HttpEntity<OrdenPago> requestEntity = new HttpEntity<>(orden, headers);
			noti.setRequest(ow.writeValueAsString(requestEntity));
			noti.setAccion("imagenesProducto");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity,
					String.class);

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setResultado(response.getBody());
				noti.setResponse(ow.writeValueAsString(responseResult));

			}

		} catch (org.springframework.web.client.HttpServerErrorException e) {
			ErrorState data = new ErrorState();
			data.setCode(e.getStatusCode().value());
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);
			try {
				noti.setResponse(ow.writeValueAsString(responseResult));
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;
		
		
		
	}

	@Override
	public ResponseOrdenPago obtenerOrdenPagoPorUsuarios(String idusuario) {
		
		ResponseOrdenPago responseResult = new ResponseOrdenPago();

		Notification noti = new Notification();

		try {
			String url = this.hostStock + "/shopping/orden/list-user?id="+idusuario;		
			URI uri = new URI(url);
			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");
			noti.setRequest("id="+idusuario);			
			noti.setAccion("obtenerOrdenPagoPorUsuarios");
			noti.setId(UUID.randomUUID().toString());
			
			ResponseEntity<List<OrdenPago>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<OrdenPago>>() {
					});

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setLstOrdenPago(response.getBody());
				noti.setResponse(ow.writeValueAsString(responseResult));

			}

		} catch (org.springframework.web.client.HttpServerErrorException e) {
			ErrorState data = new ErrorState();
			data.setCode(e.getStatusCode().value());
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);
			try {
				noti.setResponse(ow.writeValueAsString(responseResult));
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (org.springframework.web.client.HttpClientErrorException e) {
			JsonParser jsonParser = new JsonParser();
			int in = e.getLocalizedMessage().indexOf("{");
			int in2 = e.getLocalizedMessage().indexOf("}");
			String cadena = e.getMessage().substring(in, in2+1);
			JsonObject myJson = (JsonObject) jsonParser.parse(cadena);
			responseResult.setCode(myJson.get("code").getAsInt());
			ErrorState data = new ErrorState();
			data.setCode(myJson.get("code").getAsInt());
			data.setMenssage(MessageCodeImpl.getMensajeServiceCompras(myJson.get("code").getAsString() ));
			responseResult.setCode(data.getCode());
			responseResult.setError(data);
		} 
		
		
		
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;
		
		

	}

	@Override
	public ResponseResultado guardarLog(Notification noti) {
		ResponseResultado responseResult = new ResponseResultado();
		try {
			HttpHeaders headers = new HttpHeaders();
			String url = ParametrosServiceImpl.logger + "/notification";
			URI uri = new URI(url);
			HttpEntity<Notification> requestEntity = new HttpEntity<>(noti, headers);
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

			if (response.getStatusCodeValue() == 201) {
				responseResult.setCode(response.getStatusCodeValue());
				responseResult.setStatus(true);
				responseResult.setResultado(response.getBody());
				return responseResult;
			}

		} catch (org.springframework.web.client.HttpServerErrorException e) {

			ErrorState data = new ErrorState();
			data.setCode(e.getStatusCode().value());
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);
			return responseResult;

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseResult;

	}

}
