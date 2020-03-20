/*
 * Author: Tony Crespo - tonycrespo@outlook.com
 * System Engineer, Java Spring MVC, Data, Boot, Cloud Developer
 */
package com.myapp.bricolaje.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.bricolaje.model.persistency.ITipoProductoDao;
import com.myapp.bricolaje.model.repo.TipoProducto;

@Service
public class GestorTipoProducto {
	
	@Autowired
	private ITipoProductoDao tipoProductoDao;

	//-----------------------
	
	
	/**
	 * Método que da de Alta a un nuevo Tipo de Producto
	 * @param tipoProducto	Recibe un objeto del Tipo Producto
	 * @return	Devuelve 0 si se ha persistido correctamente
	 * 			4 si ha habido un fallo en la persistencia
	 * 			5 si ya existe una con anteriordidad
	 * 			la variable resultadoValidar utilizada por el método validarIntegridadDatos que devuelve deversos códigos de error
	 */			
	public int actualizarTipoProdicto(TipoProducto tipoProducto) {
		
		int resultadoValidar = validarIntegridadDatos(tipoProducto);
		
		if (resultadoValidar == 0 || resultadoValidar == 3) {
			
			Optional<TipoProducto> optTipoProducto = Optional.of(obtenerTipoProducto(tipoProducto.getIdTipoProducto()));
			
			if (optTipoProducto.isPresent()) {
				
				tipoProducto.setIdTipoProducto(optTipoProducto.get().getIdTipoProducto());
				
				Optional<TipoProducto> auxTipoProducto = Optional.of(tipoProductoDao.save(tipoProducto));
				
				if (auxTipoProducto.isPresent()) {
					
					return 0;
					
				}else {
					
					return 4; //Ha habido un fallo en la persistencia
				}
				
			}else {
				
				return 5; //El Id ya existe previamente
				
			}
		}
		
		return resultadoValidar;
	}
	
	
	/**
	 * Método que recupera un Tipo de Producto según el Id suministrado
	 * @param idTipoProducto	Recibe el Id del Tipo de Producto
	 * @return	Devuelve el objeto Tipo de Producto o null sino existe
	 */
	public TipoProducto obtenerTipoProducto(int idTipoProducto) {
		
		if (idTipoProducto != 0) {
			
			if (buscarTipoProducto(idTipoProducto) == 0) {
				
				Optional<TipoProducto> optTipoProducto = tipoProductoDao.findById(idTipoProducto);
					
					return optTipoProducto.orElse(null);
			}
		}
		
		return null;
	}
	
	/**
	 * Método que elimina un Tipo de Producto
	 * @param idTipoProducto	Recibe el Id del Tipo de Producto a eliminar de la BBDD
	 * @return	Devuelve true si fue completado correctamente o false sino
	 */
	public boolean eliminarTipoProducto(int idTipoProducto) {
		
		if (idTipoProducto != 0) {
			
			if (buscarTipoProducto(idTipoProducto) == 0){
				
				tipoProductoDao.deleteById(idTipoProducto);
				
				return true;
			}
			
		}
		
		return false;
	}
	
	/**
	 * Método utilizado para buscar un Tipo de Producto
	 * @param idTipoProducto	Recibe el Id del Tipo de Producto a ubicar en la BBDD
	 * @return	Devuelve 0 si lo consigue
	 * 			1 si no lo consigue
	 * 			2 si hemos recibido un Id inválido
	 */
	public int buscarTipoProducto(int idTipoProducto) {
		
		if (idTipoProducto != 0) {
			
			Optional<TipoProducto> optTipoProducto = tipoProductoDao.findById(idTipoProducto);
			
			if (optTipoProducto.isPresent()) {
				
				return 0;
				
			}else {
				
				return 1; //No lo consigue
			}
		}
		
		return 2; //El Id es cero
	}
	
	
	/**
	 * Método utilizado para validar que los atributos recibidos del objeto Tipo de Producto son válidos
	 * @param tipoProducto	Recibe un objeto del tipo Tipo de Producto
	 * @return	Devuelve 0 si está Ok
	 * 			1 si el Id suministrado es inválido
	 * 			2 si la descripcion es inválida
	 */
	public int validarIntegridadDatos(TipoProducto tipoProducto){
		
		if (buscarTipoProducto(tipoProducto.getIdTipoProducto()) == 0) {
			
			return 3; //El tipo de producto existe
			
		}
		
		if (tipoProducto.getIdTipoProducto() != 0) {
			
			if (tipoProducto.getDescripcion() != null) {
				
				return 0;
				
			}else {
				
				return 2; //La descripcion es inválida
			}
			
		}else {
				
			return 1; //El id es inválido
			
		}
	}
	
	/**
	 * Método utilizado para obtener una lista de todos los tipos de productos
	 * @return Una lista de objetos tipo de producto
	 */
	public List<TipoProducto> listarTodosTipoProductos() {
		
		return tipoProductoDao.findAll();
	}
}
