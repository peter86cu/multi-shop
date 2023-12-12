package com.online.multishop.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;
import org.springframework.stereotype.Service;

import com.online.multishop.modelo.*;
import com.online.multishop.vo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.online.multishop.modelo.ResponseResultado;;

@Service
public class ValidarPagoServiceImpl implements ValidarPagoService {

	public static String rutaDowloadProducto;

	private String hostStock;
	private String dlocalGo;
	private String autentication;
	public static String notificationURL;
	public static String success_pago_url;
	ObjectWriter ow = (new ObjectMapper()).writer().withDefaultPrettyPrinter();

	private boolean desarrollo = true;

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
				success_pago_url = "http://localhost:8060/shopping?iduser=";
			} else {
				cargarServer();
			}
		} catch (IOException var2) {
			System.err.println(var2.getMessage());
		}

	}

	@Override
	public ResponseValidarPago validarPagoOrden(RequestValidarPago request) throws JsonProcessingException {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseValidarPago responseAddUser = new ResponseValidarPago();

		try {
			try {
				WebTarget webTarjet = cliente.target(dlocalGo);
				Builder invoker = webTarjet.request(new String[] { "application/json" }).header("Authorization",
						"Bearer " + autentication);
				ObjectWriter ow = (new ObjectMapper()).writer().withDefaultPrettyPrinter();

				String json = ow.writeValueAsString(request);
				Logger.getLogger(ValidarPagoServiceImpl.class.getName() + " - request - validarPagoOrden").log(Level.INFO,
						json);

				response = (Response) invoker.post(Entity.entity(json, "application/json"), Response.class);
				responseJson = (String) response.readEntity(String.class);

				switch (response.getStatus()) {
				case 200:
					responseAddUser.setStatus(true);
					responseAddUser.setCode(response.getStatus());
					ValidarPagoResponse data = (new Gson()).fromJson(responseJson, ValidarPagoResponse.class);
					Logger.getLogger(ValidarPagoServiceImpl.class.getName() + " - response - validarPagoOrden")
							.log(Level.INFO, ow.writeValueAsString(data));
					responseAddUser.setPagoValido(data);
					return responseAddUser;
				case 400:
					responseAddUser.setStatus(false);
					responseAddUser.setCode(response.getStatus());
					responseAddUser.setRespuesta(responseJson);
					return responseAddUser;

				}
			} catch (JsonSyntaxException var15) {
				responseAddUser.setCode(406);
				responseAddUser.setStatus(false);
				responseAddUser.setRespuesta(var15.getMessage());
				return responseAddUser;
			} catch (ProcessingException var16) {
				responseAddUser.setCode(500);
				responseAddUser.setStatus(false);
				responseAddUser.setRespuesta(var16.getMessage());
				return responseAddUser;
			}

			return responseAddUser;
		} finally {
			if (response != null) {
				response.close();
			}

			if (cliente != null) {
				cliente.close();
			}

		}
	}

	@Override
	public int obtenerNumeroOrden() {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		int responseVal = 0;

		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/shopping/orden-number");
			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			Logger.getLogger(ValidarPagoServiceImpl.class.getName() + " - request - obtenerNumeroOrden")
					.log(Level.INFO, webTarjet.getUri().toString());

			response = builder.get();
			if (response.getStatus() == 200) {
				responseJson = (String) response.readEntity(String.class);
				Logger.getLogger(ValidarPagoServiceImpl.class.getName()).log(Level.INFO, "response - obtenerNumeroOrden()",
						responseJson);
				int data = (new Gson()).fromJson(responseJson, Integer.class);

				return data;
			}

			return responseVal;
		} catch (JsonSyntaxException var15) {
			return responseVal;
		} catch (ProcessingException var20) {
			return responseVal;
		} finally {
			if (response != null) {
				response.close();
			}

			if (cliente != null) {
				cliente.close();
			}

		}
	}

	@Override
	public ResponseResultado crearOrdenPago(OrdenPago orden) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseResultado responseOP = new ResponseResultado();

		try {
			try {
				WebTarget webTarjet = cliente.target(this.hostStock + "/shopping/orden/crear");
				Builder invoker = webTarjet.request(new String[] { "application/json" });

				try {
					String json = ow.writeValueAsString(orden);
					Logger.getLogger(ValidarPagoServiceImpl.class.getName() + " - request - crearOrdenPago").log(Level.INFO,
							json);

					response = (Response) invoker.post(Entity.entity(json, "application/json"), Response.class);
					responseJson = (String) response.readEntity(String.class);
					Logger.getLogger(ValidarPagoServiceImpl.class.getName() + " - response - crearOrdenPago").log(Level.INFO,
							ow.writeValueAsString(responseJson));
				} catch (JsonProcessingException var17) {
					Logger.getLogger(ValidarPagoServiceImpl.class.getName()).log(Level.SEVERE, (String) null, var17);
				}

				switch (response.getStatus()) {
				case 200:
					responseOP.setStatus(true);
					responseOP.setCode(response.getStatus());
					responseOP.setResultado(responseJson);
					return responseOP;
				case 400:
					responseOP.setStatus(false);
					responseOP.setCode(response.getStatus());
					responseOP.setResultado(responseJson);
					return responseOP;
				case 404:
					responseOP.setStatus(false);
					responseOP.setCode(response.getStatus());
					responseOP.setResultado(responseJson);
					return responseOP;

				}
			} catch (JsonSyntaxException var15) {
				responseOP.setCode(406);
				responseOP.setStatus(false);
				responseOP.setResultado(var15.getMessage());
				return responseOP;
			} catch (ProcessingException var16) {
				responseOP.setCode(500);
				responseOP.setStatus(false);
				responseOP.setResultado(var16.getMessage());
				return responseOP;
			}

			return responseOP;
		} finally {
			if (response != null) {
				response.close();
			}

			if (cliente != null) {
				cliente.close();
			}

		}
	}

	@Override
	public ResponseOrdenPago obtenerOrdenPagoPorUsuarios(String idusuario) {

		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";

		ResponseOrdenPago responseResult = new ResponseOrdenPago();

		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/shopping/orden/list-user?id="+idusuario);
			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			response = builder.get();
			if (response.getStatus() == 400) {
				responseResult.setStatus(false);
				responseResult.setCode(response.getStatus());
				responseResult.setResultado(response.readEntity(String.class));
				return responseResult;
			}

			responseJson = (String) response.readEntity(String.class);
			responseResult.setStatus(true);
			responseResult.setCode(response.getStatus());
			OrdenPago[] data = (OrdenPago[]) (new Gson()).fromJson(responseJson, OrdenPago[].class);
			responseResult.setLstOrdenPago(data);
			return responseResult;

		} catch (JsonSyntaxException var15) {
			responseResult.setCode(406);
			responseResult.setStatus(false);
			responseResult.setResultado(var15.getMessage());
			return responseResult;
		} catch (ProcessingException var16) {
			responseResult.setCode(500);
			responseResult.setStatus(false);
			responseResult.setResultado(var16.getMessage());

			return responseResult;
		} finally {
			if (response != null) {
				response.close();
			}

			if (cliente != null) {
				cliente.close();
			}

		}

	}

}
