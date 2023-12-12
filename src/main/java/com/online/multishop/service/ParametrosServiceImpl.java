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

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.online.multishop.modelo.*;
import com.online.multishop.modelo.ResponseResultado;
import com.online.multishop.vo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;



@Service
public class ParametrosServiceImpl implements ParametrosService {

	
	
	public static String rutaDowloadProducto;
	private  String hostStock;	
	private boolean desarrollo=true;
	
	
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
				this.rutaDowloadProducto = p.getProperty("server.uploaderProductos");
				
			}
		} catch (FileNotFoundException var3) {
			System.err.println(var3.getMessage());
		}

	}

	public ParametrosServiceImpl() {
		try {
			if(desarrollo){
				hostStock = "http://localhost:8082";
				rutaDowloadProducto="C:\\xampp\\htdocs\\multishop\\img\\";
			}else{
				cargarServer();
			}
		} catch (IOException var2) {
			System.err.println(var2.getMessage());
		}

	}
	
	@Cacheable(cacheNames = "tipoProducto")
	@Override
	public ResponseTipoProducto listadoTipoProducto() {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";

		ResponseTipoProducto responseResult = new ResponseTipoProducto();
		
		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/parametros/tipoproducto");

			Builder builder = webTarjet.request(new String[] { "application/json" });
			Logger.getLogger(ParametrosServiceImpl.class.getName()+" - request - listadoTipoProducto").log(Level.INFO,  webTarjet.getUri().toString());

			builder.accept(new String[] { "application/json" });
			response = builder.get();
			System.out.println("Lllego bien: "+ response.getStatusInfo());
			if (response.getStatus() == 200) {
				responseJson = (String) response.readEntity(String.class);
				Logger.getLogger(ParametrosServiceImpl.class.getName()+" - request - listadoTipoProducto").log(Level.WARNING,  responseJson);

				responseResult.setStatus(true);
				responseResult.setCode(response.getStatus());
				TipoProducto[] data = (TipoProducto[]) (new Gson()).fromJson(responseJson, TipoProducto[].class);
				responseResult.setTipoProductos(data);
				System.out.println("Lllego bien: "+ responseJson);

				return responseResult;
			}
			System.out.println("Lllego mal "+ response.getStatus());
			responseResult.setStatus(false);
			responseResult.setCode(response.getStatus());
			responseResult.setResultado(response.readEntity(String.class));
			Logger.getLogger(ParametrosServiceImpl.class.getName()+" - request - listadoTipoProducto").log(Level.WARNING,  responseResult.getResultado());

			return responseResult;
			

		} catch (JsonSyntaxException var15) {
			responseResult.setCode(406);
			responseResult.setStatus(false);
			responseResult.setResultado(var15.getMessage());
			System.out.println(var15.getMessage());
			return responseResult;
		} catch (ProcessingException var16) {
			responseResult.setCode(500);
			responseResult.setStatus(false);
			responseResult.setResultado(var16.getMessage());
			System.out.println(var16.getMessage());

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
	
	@Cacheable(cacheNames = "categorias")
	@Override
	public ResponseCategorias listarCategorias() {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseCategorias responseResult = new ResponseCategorias();

		ResponseCategorias var11;
		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/parametros/categoria");
			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			response = builder.get();
			
				if (response.getStatus() == 200) {
					responseJson = (String) response.readEntity(String.class);
					responseResult.setStatus(true);
					responseResult.setCode(response.getStatus());
					Categoria[] data = (Categoria[]) (new Gson()).fromJson(responseJson, Categoria[].class);
					responseResult.setCategorias(data);
					return responseResult;
				}

				responseResult.setStatus(false);
				responseResult.setCode(response.getStatus());
				responseResult.setResultado(response.readEntity(String.class));
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
			var11 = responseResult;
			return var11;
		} finally {
			if (response != null) {
				response.close();
			}

			if (cliente != null) {
				cliente.close();
			}

		}
	}
	
	@Cacheable(cacheNames="marcas")
	@Override
	public ResponseListaMarcasProducto listadoMarcasProducto() {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";

		ResponseListaMarcasProducto responseResult = new ResponseListaMarcasProducto();

		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/parametros/marcas");
			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			response = builder.get();
			if (response.getStatus() == 400 || response.getStatus() == 406 || response.getStatus() == 404) {

				responseResult.setStatus(false);
				responseResult.setCode(response.getStatus());
				responseJson = (String) response.readEntity(String.class);
				responseResult.setResultado(responseJson);
				return responseResult;
			}

			responseJson = (String) response.readEntity(String.class);
			responseResult.setStatus(true);
			responseResult.setCode(response.getStatus());
			MarcaProducto[] data =  new Gson().fromJson(responseJson, MarcaProducto[].class);
			responseResult.setMarcas(data);
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
	
	@Cacheable(cacheNames="modelos")
	@Override
	public ResponseListaModeloProducto listadoModelosProducto() {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";

		ResponseListaModeloProducto responseResult = new ResponseListaModeloProducto();

		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/parametros/modelos");
			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			response = builder.get();
			if (response.getStatus() == 400 || response.getStatus() == 406 || response.getStatus() == 404) {

				responseResult.setStatus(false);
				responseResult.setCode(response.getStatus());
				responseJson = (String) response.readEntity(String.class);
				responseResult.setResultado(responseJson);
				return responseResult;
			}

			responseJson = (String) response.readEntity(String.class);
			responseResult.setStatus(true);
			responseResult.setCode(response.getStatus());
			ModeloProducto[] data =  new Gson().fromJson(responseJson, ModeloProducto[].class);
			responseResult.setModelo(data);
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
	public ResponseListaProductos consultarListaProductos() {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		
		ResponseListaProductos responseResult = new ResponseListaProductos();

		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/productos/lista");
			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			response = builder.get();
			if (response.getStatus() == 400 || response.getStatus() == 406) {
				responseResult.setStatus(false);
				responseResult.setCode(response.getStatus());
				responseJson = (String) response.readEntity(String.class);
				responseResult.setResultado(responseJson);
				return responseResult;
			}
			responseJson = (String) response.readEntity(String.class);
			responseResult.setStatus(true);
			responseResult.setCode(response.getStatus());
			Producto[] data = (Producto[]) (new Gson()).fromJson(responseJson, Producto[].class);
			responseResult.setProductos(data);
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
	public ResponseMonedas listarMonedas() {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";

		ResponseMonedas responseResult = new ResponseMonedas();

		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/parametros/monedas");
			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			response = builder.get();
			if (response.getStatus() == 400 || response.getStatus() == 406 || response.getStatus() == 404) {

				responseResult.setStatus(false);
				responseResult.setCode(response.getStatus());
				responseJson = (String) response.readEntity(String.class);
				responseResult.setResultado(responseJson);
				return responseResult;
			}

			responseJson = (String) response.readEntity(String.class);
			responseResult.setStatus(true);
			responseResult.setCode(response.getStatus());
			Moneda[] data = (Moneda[]) (new Gson()).fromJson(responseJson, Moneda[].class);
			responseResult.setMonedas(data);
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
	public ProductoImagenes[] imagenesProducto(String id) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ProductoImagenes result[]= new ProductoImagenes[0];
		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/productos/imagenes?id="+id);
			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			response = builder.get();
			
				if (response.getStatus() == 200) {
					responseJson = (String) response.readEntity(String.class);
					ProductoImagenes[] data = (ProductoImagenes[]) (new Gson()).fromJson(responseJson, ProductoImagenes[].class);
				
					return data;
				}

				
				return result;
			
		} catch (JsonSyntaxException var15) {
			
			return result;
		} catch (ProcessingException var16) {
			
			return result;
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
	public ResponseDetalleProducto detalleProducto(String id) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseDetalleProducto responseResult=new ResponseDetalleProducto();
		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/productos/detalle?id="+id);
			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			response = builder.get();
			
			if (response.getStatus() == 400 || response.getStatus() == 406 || response.getStatus() == 404) {

				responseResult.setStatus(false);
				responseResult.setCode(response.getStatus());
				responseJson = (String) response.readEntity(String.class);
				responseResult.setResultado(responseJson);
				return responseResult;
			}

			responseJson = (String) response.readEntity(String.class);
			responseResult.setStatus(true);
			responseResult.setCode(response.getStatus());
			ProductoDetalles data =  new Gson().fromJson(responseJson, ProductoDetalles.class);
			responseResult.setDetalle(data);
			return responseResult;
				
			
		}catch (JsonSyntaxException var15) {
			responseResult.setCode(406);
			responseResult.setStatus(false);
			responseResult.setResultado(var15.getMessage());
			return responseResult;

		} catch (ProcessingException var16) {
			responseResult.setCode(500);
			responseResult.setStatus(false);
			responseResult.setResultado(var16.getMessage());
			return responseResult;
		}   finally {
			if (response != null) {
				response.close();
			}

			if (cliente != null) {
				cliente.close();
			}

		}
	}

	@Override
	public ResponseResultado guardarCarrito(RequestAddCart request) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseResultado responseAddProduct = new ResponseResultado();

		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/shopping/guardar-cart");
			Builder builder = webTarjet.request(new String[] { "application/json" });
			ObjectWriter ow = (new ObjectMapper()).writer().withDefaultPrettyPrinter();
			builder.accept(new String[] { "application/json" });

			try {
				String json = ow.writeValueAsString(request);
				response = (Response) builder.post(Entity.entity(json, "application/json"), Response.class);
				responseJson = (String) response.readEntity(String.class);
			} catch (JsonProcessingException var16) {
				Logger.getLogger(ParametrosServiceImpl.class.getName()).log(Level.SEVERE, (String) null, var16);
			}

			switch (response.getStatus()) {
			case 200:
				responseAddProduct.setStatus(true);
				responseAddProduct.setCode(response.getStatus());
				responseAddProduct.setResultado(responseJson);
				return responseAddProduct;
			case 400:
				responseAddProduct.setStatus(false);
				responseAddProduct.setCode(response.getStatus());
				responseAddProduct.setResultado(responseJson);
				return responseAddProduct;
			case 406:
				responseAddProduct.setStatus(false);
				responseAddProduct.setCode(response.getStatus());
				responseAddProduct.setResultado(responseJson);
				return responseAddProduct;

			}
		} catch (JsonSyntaxException var15) {
			responseAddProduct.setCode(406);
			responseAddProduct.setStatus(false);
			responseAddProduct.setResultado(var15.getMessage());
			return responseAddProduct;
		} catch (ProcessingException var16) {
			responseAddProduct.setCode(500);
			responseAddProduct.setStatus(false);
			responseAddProduct.setResultado(var16.getMessage());
			return responseAddProduct;
		} finally {
			if (response != null) {
				response.close();
			}

			if (cliente != null) {
				cliente.close();
			}

		}

		return responseAddProduct;
	}

	@Override
	public ResponseCart obtenerCarrito(String idCart,String idUsuario) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseCart result= new ResponseCart();
		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/shopping/obtener-cart?idcart="+idCart+"&idusuario="+idUsuario);
			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			response = builder.get();
			
				if (response.getStatus() == 200) {
					responseJson = (String) response.readEntity(String.class);
					ResponseCart data = (ResponseCart) (new Gson()).fromJson(responseJson, ResponseCart.class);
				
					return data;
				}

				
				return result;
			
		} catch (JsonSyntaxException var15) {
			
			return result;
		} catch (ProcessingException var16) {
			
			return result;
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
	public ResponseCart[] obtenerCarritoPorUsuario(String idUsuario) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ResponseCart result[]= new ResponseCart[0];
		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/shopping/obtener-cart-usuario?idusuario="+idUsuario);
			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			response = builder.get();
			
				if (response.getStatus() == 200) {
					responseJson = (String) response.readEntity(String.class);					
					ResponseCart[] data = (new Gson()).fromJson(responseJson, ResponseCart[].class);
				
					return data;
				}

				
				return result;
			
		} catch (JsonSyntaxException var15) {
			
			return result;
		} catch (ProcessingException var16) {
			
			return result;
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
	public ShoppingHistoryEstado obtenerEstadoCarrito(int id) {
		Response response = null;
		Client cliente = ClientBuilder.newClient();
		String responseJson = "";
		ShoppingHistoryEstado result= new ShoppingHistoryEstado();
		try {
			WebTarget webTarjet = cliente.target(this.hostStock + "/shopping/obtener-estado-cart?id="+id);
			Builder builder = webTarjet.request(new String[] { "application/json" });
			builder.accept(new String[] { "application/json" });
			response = builder.get();
			
				if (response.getStatus() == 200) {
					responseJson = (String) response.readEntity(String.class);					
					ShoppingHistoryEstado data = (new Gson()).fromJson(responseJson, ShoppingHistoryEstado.class);
				
					return data;
				}

				
				return result;
			
		} catch (JsonSyntaxException var15) {
			
			return result;
		} catch (ProcessingException var16) {
			
			return result;
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
