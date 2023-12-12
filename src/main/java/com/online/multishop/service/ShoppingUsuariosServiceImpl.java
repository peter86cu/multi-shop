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
import org.springframework.web.client.RestTemplate;

import com.online.multishop.vo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.online.multishop.modelo.*;
import com.online.multishop.modelo.ResponseResultado;

@Service
public class ShoppingUsuariosServiceImpl implements ShoppingUsuariosService {

	public String hostSeguridad;

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
				this.hostSeguridad = p.getProperty("server.seguridad");

			}
		} catch (FileNotFoundException var3) {
			System.err.println(var3.getMessage());
		}

	}

	public ShoppingUsuariosServiceImpl() {
		try {
			if (desarrollo) {
				hostSeguridad = "http://localhost:7001";
			} else {
				cargarServer();
			}
		} catch (IOException var2) {
			System.err.println(var2.getMessage());
		}

	}

	@Override
	public ResponseResultado crearUsuario(ShoppingUsuarios usuario, String token) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseResultado responseAddUser = new ResponseResultado();

		try {
			try {
				WebTarget webTarjet = cliente.target(this.hostSeguridad + "/shopping/usuario/crear");
				Builder invoker = webTarjet.request(new String[] { "application/json" }).header("Authorization",
						"Bearer " + token);
				ObjectWriter ow = (new ObjectMapper()).writer().withDefaultPrettyPrinter();

				try {
					String json = ow.writeValueAsString(usuario);
					Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+ " - request - crearUsuario").log(Level.INFO, json);

					response = (Response) invoker.post(Entity.entity(json, "application/json"), Response.class);
					responseJson = (String) response.readEntity(String.class);
					Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName() +" - response - crearUsuario").log(Level.INFO, responseJson);

				} catch (JsonProcessingException var17) {
					Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()).log(Level.SEVERE, (String) null,
							var17);
				}

				switch (response.getStatus()) {
				case 200:
					responseAddUser.setStatus(true);
					responseAddUser.setCode(response.getStatus());
					responseAddUser.setResultado(responseJson);
					return responseAddUser;
				case 400:
					responseAddUser.setStatus(false);
					responseAddUser.setCode(response.getStatus());
					responseAddUser.setResultado(responseJson);
					return responseAddUser;

				}
			} catch (JsonSyntaxException var15) {
				responseAddUser.setCode(406);
				responseAddUser.setStatus(false);
				responseAddUser.setResultado(var15.getMessage());
				return responseAddUser;
			} catch (ProcessingException var16) {
				responseAddUser.setCode(500);
				responseAddUser.setStatus(false);
				responseAddUser.setResultado(var16.getMessage());
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
	public ResponseResultado obtenerToken(String mail, String pwd) {
		Response response = null;
		Client cliente = ClientBuilder.newBuilder().build();
		ResponseResultado responseResult = new ResponseResultado();

		try {
			String responseJson = "";
			WebTarget webTarjet = cliente.target(this.hostSeguridad + "/login/token?mail=" + mail + "&pwd=" + pwd);
			Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - request - obtenerToken").log(Level.INFO,  webTarjet.getUri().toString());

			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			response = builder.post(null);
			Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - request - obtenerToken").log(Level.INFO,  webTarjet.getUri().toString());

			if (response.getStatus() == 400 || response.getStatus() == 404) {
				responseResult.setCode(response.getStatus());
				responseResult.setStatus(false);
				String errorW = (String) response.readEntity(String.class);
				Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName() +" response - obtenerToken").log(Level.WARNING, errorW);
				responseResult.setResultado(errorW);
				return responseResult;
			}
			responseJson = (String) response.readEntity(String.class);
			responseResult.setCode(response.getStatus());
			responseResult.setStatus(true);
			responseResult.setResultado(responseJson);
			Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName() +" - response - obtenerToken").log(Level.INFO,responseJson);
			return responseResult;
		} catch (JsonSyntaxException var16) {
			responseResult.setCode(406);
			responseResult.setStatus(false);
			responseResult.setResultado(var16.getMessage());
			return responseResult;
		} catch (ProcessingException var17) {
			responseResult.setCode(500);
			responseResult.setStatus(false);
			responseResult.setResultado(var17.getCause().getMessage());
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

	public ResponseResultado validarToken(String token) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseResultado responseResult = new ResponseResultado();

		try {
			WebTarget webTarjet = cliente.target(this.hostSeguridad + "/login/validar");
			Builder invoker = webTarjet.request(new String[] { null, "application/json" }).header("Authorization",
					"Bearer " + token);
			Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - request - validarToken").log(Level.INFO,  webTarjet.getUri().toString());

			response = invoker.post(null);
			if (response.getStatus() == 400) {
				responseResult.setCode(response.getStatus());
				responseResult.setStatus(false);
				responseResult.setResultado((String) response.readEntity(String.class));
				Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - response - validarToken").log(Level.WARNING,  (String) response.readEntity(String.class));

			} else {
				responseJson = (String) response.readEntity(String.class);
				responseResult.setCode(response.getStatus());
				responseResult.setStatus(true);
				responseResult.setResultado(responseJson);
				Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - responbse - obtenerToken").log(Level.INFO,  responseJson);

			}

			return responseResult;
		} catch (JsonSyntaxException var9) {
			responseResult.setCode(406);
			responseResult.setStatus(false);
			responseResult.setResultado(var9.getMessage());
			return responseResult;
		} catch (ProcessingException var20) {
			responseResult.setCode(500);
			responseResult.setStatus(false);
			responseResult.setResultado(var20.getCause().getCause().getMessage());
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

	@Override
	public ResponseUsuario obtenerDatosUsuarioLogin(String token, String mail) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseUsuario responseUser = new ResponseUsuario();

		try {
			WebTarget webTarjet = cliente.target(this.hostSeguridad + "/shopping/usuario/buscar?mail=" + mail);
			Builder builder = webTarjet.request(new String[] { "application/json" }).header("Authorization",
					"Bearer " + token);
			Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - request - obtenerDatosUsuarioLogin").log(Level.INFO,  webTarjet.getUri().toString());

			builder.accept(new String[] { "application/json" });
			response = builder.get();
			if (response.getStatus() == 200) {
				responseJson = (String) response.readEntity(String.class);
				Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - response - obtenerDatosUsuarioLogin").log(Level.INFO,  responseJson);

				responseUser.setStatus(true);
				responseUser.setCode(response.getStatus());
				ShoppingUsuarios data = (ShoppingUsuarios) (new Gson()).fromJson(responseJson, ShoppingUsuarios.class);
				responseUser.setUser(data);

				return responseUser;
			}

			responseUser.setStatus(false);
			responseUser.setCode(response.getStatus());
			responseUser.setResultado((String) response.readEntity(String.class));
			Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - response - obtenerDatosUsuarioLogin").log(Level.WARNING,  (String) response.readEntity(String.class));

			return responseUser;
		} catch (JsonSyntaxException var15) {
			responseUser.setCode(406);
			responseUser.setStatus(false);
			responseUser.setResultado(var15.getMessage());
			return responseUser;
		} catch (ProcessingException var20) {
			responseUser.setCode(500);
			responseUser.setStatus(false);
			responseUser.setResultado(var20.getCause().getCause().getMessage());
			return responseUser;
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
	public ResponseUsuario buscarUsuarioPorId(String token, String id) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseUsuario responseUser = new ResponseUsuario();

		try {
			WebTarget webTarjet = cliente.target(this.hostSeguridad + "/shopping/usuario/id-usuario?id=" + id);
			Builder builder = webTarjet.request(new String[] { "application/json" }).header("Authorization",
					"Bearer " + token);
			Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - request - buscarUsuarioPorId").log(Level.INFO,  webTarjet.getUri().toString());

			builder.accept(new String[] { "application/json" });
			response = builder.get();
			if (response.getStatus() == 200) {
				responseJson = (String) response.readEntity(String.class);
				responseUser.setStatus(true);
				responseUser.setCode(response.getStatus());
				ShoppingUsuarios data = (ShoppingUsuarios) (new Gson()).fromJson(responseJson, ShoppingUsuarios.class);
				responseUser.setUser(data);
				Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - response - buscarUsuarioPorId").log(Level.INFO, responseJson);

				return responseUser;
			}
			responseUser.setStatus(false);
			responseUser.setCode(response.getStatus());
			responseJson = (String) response.readEntity(String.class);
			responseUser.setResultado(responseJson);
			Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - response - buscarUsuarioPorId").log(Level.WARNING,  (String) response.readEntity(String.class));

			return responseUser;

		} catch (JsonSyntaxException var15) {
			responseUser.setCode(406);
			responseUser.setStatus(false);
			responseUser.setResultado(var15.getMessage());
			return responseUser;
		} catch (ProcessingException var16) {
			responseUser.setCode(500);
			responseUser.setStatus(false);
			responseUser.setResultado(var16.getMessage());
			return responseUser;
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
	public ResponseUsuario validarUsuario(String mail, String token) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseUsuario responseUser = new ResponseUsuario();

		try {
			WebTarget webTarjet = cliente.target(this.hostSeguridad + "/shopping/usuario/buscar?mail=" + mail);
			Builder invoker = webTarjet.request(new String[] { "application/json" }).header("Authorization",
					"Bearer " + token);
			Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - request - validarUsuario").log(Level.INFO,  webTarjet.getUri().toString());

			response = invoker.get();
			if (response.getStatus() == 200) {
				responseJson = (String) response.readEntity(String.class);
				responseUser.setStatus(true);
				responseUser.setCode(response.getStatus());
				ShoppingUsuarios data = (ShoppingUsuarios) (new Gson()).fromJson(responseJson, ShoppingUsuarios.class);
				Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - response - validarUsuario").log(Level.INFO,  responseJson);

				responseUser.setUser(data);
				return responseUser;
			}

			responseUser.setStatus(false);
			responseUser.setCode(response.getStatus());
			responseUser.setResultado((String) response.readEntity(String.class));
			Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - response - validarUsuario").log(Level.WARNING,  (String) response.readEntity(String.class));

			return responseUser;
		} catch (JsonSyntaxException var15) {
			responseUser.setCode(406);
			responseUser.setStatus(false);
			responseUser.setResultado(var15.getMessage());
			return responseUser;
		} catch (ProcessingException var16) {
			responseUser.setCode(500);
			responseUser.setStatus(false);
			responseUser.setResultado(var16.getMessage());
			return responseUser;
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
	public String salir(RestTemplate template) {
		return (String) template.postForObject(hostSeguridad + "/login/salir", null, String.class, new Object[0]);
	}

	@Override
	public ResponseResultado cambiarPassword(String idUsuario, String pass, String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseResultado guardarDireccionUsuario(DireccionUsuario dir, String token) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseResultado responseAddUser = new ResponseResultado();

		try {
			try {
				WebTarget webTarjet = cliente.target(this.hostSeguridad + "/shopping/direccion/crear");
				Builder invoker = webTarjet.request(new String[] { "application/json" }).header("Authorization",
						"Bearer " + token);
				ObjectWriter ow = (new ObjectMapper()).writer().withDefaultPrettyPrinter();

				try {
					String json = ow.writeValueAsString(dir);
					Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - rquest - guardarDireccionUsuario").log(Level.INFO,  json);

					response = (Response) invoker.post(Entity.entity(json, "application/json"), Response.class);
					responseJson = (String) response.readEntity(String.class);
					Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - response - guardarDireccionUsuario").log(Level.INFO,  responseJson);

				} catch (JsonProcessingException var17) {
					Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()).log(Level.SEVERE, (String) null,
							var17);
				}

				switch (response.getStatus()) {
				case 200:
					responseAddUser.setStatus(true);
					responseAddUser.setCode(response.getStatus());
					responseAddUser.setResultado(responseJson);
					return responseAddUser;
				case 400:
					responseAddUser.setStatus(false);
					responseAddUser.setCode(response.getStatus());
					responseAddUser.setResultado(responseJson);
					return responseAddUser;

				}
			} catch (JsonSyntaxException var15) {
				responseAddUser.setCode(406);
				responseAddUser.setStatus(false);
				responseAddUser.setResultado(var15.getMessage());
				return responseAddUser;
			} catch (ProcessingException var16) {
				responseAddUser.setCode(500);
				responseAddUser.setStatus(false);
				responseAddUser.setResultado(var16.getMessage());
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
	public ResponseDirecciones recuperarDreccionUsuarioPorId(String idUsuario, String token) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseDirecciones responseResult = new ResponseDirecciones();

		try {
			WebTarget webTarjet = cliente.target(this.hostSeguridad + "/shopping/direccion/buscar?id=" + idUsuario);
			Builder builder = webTarjet.request(new String[] { "application/json" }).header("Authorization",
					"Bearer " + token);
			Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - request - recuperarDreccionUsuarioPorId").log(Level.INFO,  webTarjet.getUri().toString());

			builder.accept(new String[] { "application/json" });
			response = builder.get();

			if (response.getStatus() == 200) {
				responseJson = (String) response.readEntity(String.class);
				responseResult.setStatus(true);
				responseResult.setCode(response.getStatus());
				DireccionUsuario[] data = (new Gson()).fromJson(responseJson, DireccionUsuario[].class);
				responseResult.setDirecciones(data);
				Logger.getLogger(ShoppingUsuariosServiceImpl.class.getName()+" - response - recuperarDreccionUsuarioPorId").log(Level.INFO,  responseJson);

				return responseResult;
			}
			responseResult.setStatus(false);
			responseResult.setCode(response.getStatus());
			responseJson = (String) response.readEntity(String.class);
			responseResult.setResultado(responseJson);
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

	@Override
	public ResponseResultado eliminarDreccionUsuarioPorId(int id, String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
