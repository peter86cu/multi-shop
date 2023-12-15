package com.online.multishop.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.online.multishop.modelo.*;
import com.online.multishop.modelo.ResponseResultado;
import com.online.multishop.vo.*;
import com.ayalait.fecha.FormatearFechas;
import com.ayalait.logguerclass.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@Service
public class ParametrosServiceImpl implements ParametrosService {

	public static String rutaDowloadProducto;
	private String hostStock;
	public static String logger;
	private boolean desarrollo = false;
	ObjectWriter ow = (new ObjectMapper()).writer().withDefaultPrettyPrinter();

	@Autowired
	RestTemplate restTemplate;

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
				this.logger = p.getProperty("server.logger");
				this.rutaDowloadProducto = p.getProperty("server.uploaderProductos");

			}
		} catch (FileNotFoundException var3) {
			System.err.println(var3.getMessage());
		}

	}

	public ParametrosServiceImpl() {
		try {
			if (desarrollo) {
				hostStock = "http://localhost:8082";
				logger = "http://localhost:8086";
				rutaDowloadProducto = "C:\\xampp\\htdocs\\multishop\\img\\";
			} else {
				cargarServer();
			}
		} catch (IOException var2) {
			System.err.println(var2.getMessage());
		}

	}

	@Override
	public ResponseResultado guardarLog(Notification noti) {

		ResponseResultado responseResult = new ResponseResultado();
		try {
			HttpHeaders headers = new HttpHeaders();
			String url = this.logger + "/notification";

			HttpEntity<Notification> requestEntity = new HttpEntity<>(noti, headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class,
					new Object[0]);

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

		}

		return responseResult;

	}

	@Cacheable(cacheNames = "tipoProducto")
	@Override
	public ResponseTipoProducto listadoTipoProducto() {

		ResponseTipoProducto responseResult = new ResponseTipoProducto();
		Notification noti = new Notification();

		try {

			String url = this.hostStock + "/parametros/tipoproducto";

			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");
			noti.setAccion("listadoTipoProducto");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<List<TipoProducto>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<TipoProducto>>() {
					});

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setTipoProductos(response.getBody());
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
		}
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;

	}

	@Cacheable(cacheNames = "categorias")
	@Override
	public ResponseCategorias listarCategorias() {

		ResponseCategorias responseResult = new ResponseCategorias();
		Notification noti = new Notification();

		try {

			String url = this.hostStock + "/parametros/categoria";

			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");

			noti.setAccion("listarCategorias");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<List<Categoria>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Categoria>>() {
					});

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setCategorias(response.getBody());
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
		}
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;
	}

	@Cacheable(cacheNames = "marcas")
	@Override
	public ResponseListaMarcasProducto listadoMarcasProducto() {

		ResponseListaMarcasProducto responseResult = new ResponseListaMarcasProducto();

		Notification noti = new Notification();

		try {

			String url = this.hostStock + "/parametros/marcas";

			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");

			noti.setAccion("listadoMarcasProducto");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<List<MarcaProducto>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<MarcaProducto>>() {
					});

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setMarcas(response.getBody());
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
		}
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;

	}

	@Cacheable(cacheNames = "modelos")
	@Override
	public ResponseListaModeloProducto listadoModelosProducto() {

		ResponseListaModeloProducto responseResult = new ResponseListaModeloProducto();

		Notification noti = new Notification();

		try {

			String url = this.hostStock + "/parametros/modelos";

			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");

			noti.setAccion("listadoModelosProducto");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<List<ModeloProducto>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<ModeloProducto>>() {
					});

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setModelo(response.getBody());
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
		}
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;

	}

	@Override
	public ResponseListaProductos consultarListaProductos() {

		ResponseListaProductos responseResult = new ResponseListaProductos();
		Notification noti = new Notification();

		try {

			String url = this.hostStock + "/productos/lista";

			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");

			noti.setAccion("consultarListaProductos");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<List<Producto>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Producto>>() {
					});

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setProductos(response.getBody());
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
		}
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;

	}

	@Override
	public ResponseMonedas listarMonedas() {
		
		ResponseMonedas responseResult = new ResponseMonedas();

		Notification noti = new Notification();

		try {

			String url = this.hostStock + "/parametros/monedas";

			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");

			noti.setAccion("listarMonedas");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<List<Moneda>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Moneda>>() {
					});

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setMonedas(response.getBody());
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
		}
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;
		
		

	}

	@Override
	public ResponseImagenesProducto imagenesProducto(String id) {
		
		ResponseImagenesProducto responseResult = new ResponseImagenesProducto();
		Notification noti = new Notification();

		try {
			String url = this.hostStock + "/productos/imagenes?id=" + id;
			
			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");
			noti.setRequest(id);
			noti.setAccion("imagenesProducto");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<List<ProductoImagenes>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<ProductoImagenes>>() {
					});

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setImagenProducto(response.getBody());
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
		}
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;		
		
	}

	@Override
	public ResponseDetalleProducto detalleProducto(String id) {
		
		ResponseDetalleProducto responseResult = new ResponseDetalleProducto();
		
		Notification noti = new Notification();

		try {
			String url = this.hostStock + "/productos/detalle?id=" + id;		
			
			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");
			noti.setRequest(id);
			noti.setAccion("detalleProducto");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<ProductoDetalles> response=null;
			try {
				 response = restTemplate.exchange(url, HttpMethod.GET, null,
						ProductoDetalles.class);
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
				noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
				ResponseResultado result = guardarLog(noti);
				if (!result.isStatus()) {
					System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
				}

				return responseResult;	
			}catch (org.springframework.web.client.HttpClientErrorException e) {
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
				noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
				ResponseResultado result = guardarLog(noti);
				if (!result.isStatus()) {
					System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
				}

				return responseResult;	
			}
			
			

			if (response.getStatusCodeValue() == 200) {				
				responseResult.setStatus(true);
				responseResult.setDetalle(response.getBody());
				noti.setResponse(ow.writeValueAsString(responseResult));

			}

		} catch (Exception e) {
			ErrorState data = new ErrorState();
			data.setCode(500);
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);
			try {
				noti.setResponse(ow.writeValueAsString(responseResult));
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;	
		
		
		
	}

	@Override
	public ResponseResultado guardarCarrito(RequestAddCart request) {
		
		ResponseResultado responseResult = new ResponseResultado();
		
		Notification noti = new Notification();

		try {
			String url = this.hostStock + "/shopping/guardar-cart";		
			HttpHeaders headers = new HttpHeaders();

			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");
			HttpEntity<RequestAddCart> requestEntity = new HttpEntity<>(request, headers);

			noti.setAccion("imagenesProducto");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
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
		}
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;

		
	}

	@Override
	public ResponseCart obtenerCarrito(String idCart, String idUsuario) {
		
		ResponseCart responseResult = new ResponseCart();	
		Notification noti = new Notification();

		try {
			String url = this.hostStock + "/shopping/obtener-cart?idcart=" + idCart + "&idusuario=" + idUsuario;		

			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");
			noti.setRequest("idcart=" + idCart + "&idusuario=" + idUsuario);
			
			noti.setAccion("obtenerCarrito");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<CarritoDetalle> response = restTemplate.exchange(url, HttpMethod.GET, null,
					CarritoDetalle.class);

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setCartDetalle(response.getBody());
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
		}
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;
		
		
		
	}

	@Override
	public ResponseCartUsuario obtenerCarritoPorUsuario(String idUsuario) {
		
		ResponseCartUsuario responseResult = new ResponseCartUsuario();
		
		Notification noti = new Notification();

		try {
			String url = this.hostStock + "/shopping/obtener-cart-usuario?idusuario=" + idUsuario;		

			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");
			noti.setRequest("idusuario=" + idUsuario);
			
			noti.setAccion("obtenerCarritoPorUsuario");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<List<CarritoDetalle>> response=null;
			try {
				 response = restTemplate.exchange(url, HttpMethod.GET, null,
						new ParameterizedTypeReference<List<CarritoDetalle>>() {
						});
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
				noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
				ResponseResultado result = guardarLog(noti);
				if (!result.isStatus()) {
					System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
				}

				return responseResult;
			}
			
			

			if (response.getStatusCodeValue() == 200) {
				responseResult.setCode(response.getStatusCodeValue());
				responseResult.setStatus(true);
				responseResult.setCartDetalle(response.getBody());
				noti.setResponse(ow.writeValueAsString(responseResult));

			}

		} catch (Exception e) {
			ErrorState data = new ErrorState();
			data.setCode(500);
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);
			try {
				noti.setResponse(ow.writeValueAsString(responseResult));
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;
		
		
		
	}

	@Override
	public ResponseHistoryEstadoCart obtenerEstadoCarrito(int id) {
		
		ResponseHistoryEstadoCart responseResult = new ResponseHistoryEstadoCart();		
		Notification noti = new Notification();

		try {
			String url = this.hostStock + "/shopping/obtener-estado-cart?id=" + id;		
			
			noti.setFecha_inicio(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
			noti.setClass_id("multishop-APP");
			noti.setRequest("id=" + id);
			noti.setAccion("obtenerEstadoCarrito");
			noti.setId(UUID.randomUUID().toString());
			ResponseEntity<ShoppingHistoryEstado> response = restTemplate.exchange(url, HttpMethod.GET, null,
					ShoppingHistoryEstado.class);

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setEstado(response.getBody());
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
		}
		noti.setFecha_fin(FormatearFechas.obtenerFechaPorFormato("yyyy-MM-dd hh:mm:ss"));
		ResponseResultado result = guardarLog(noti);
		if (!result.isStatus()) {
			System.err.println(result.getError().getCode() + " " + result.getError().getMenssage());
		}

		return responseResult;	
		
		
		
	}

}
