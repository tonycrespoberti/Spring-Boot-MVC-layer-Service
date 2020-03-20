/*
 * Author: Tony Crespo - tonycrespo@outlook.com
 * System Engineer, Java Spring MVC, Data, Boot, Cloud Developer
 */
package com.myapp.bricolaje.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.bricolaje.model.persistency.IUnidadVentaDao;
import com.myapp.bricolaje.model.repo.UnidadVenta;

@Service
public class GestorUnidadVenta {

	@Autowired
	private IUnidadVentaDao unidadVentaDao;

	//----------------------
	
	
	/**
	 * Método que da de alta a una Unidad de Venta
	 * @param unidadVenta	Recibe el nuevo objeto a persistir
	 * @return	Devuelve 0 si se ha agregado correctamente
	 * 			4 si ha habido un fallo intentando persistir el objeto
	 * 			resultadoValidar variable que devuelve el método validarIntegridadDatos.
	 */
	public int agregarUnidadVenta(UnidadVenta unidadVenta) {
		
		int resultadoValidar = validarIntregidadDatos(unidadVenta);
		
		if (resultadoValidar == 0) {
			
			Optional<UnidadVenta> optUnidadVenta = Optional.of(unidadVentaDao.save(unidadVenta));
			
			if (optUnidadVenta.isPresent()) {
				
				return 0;
				
			}else {
				
				return 4; //Ha habido un fallo persistiendo el obejto
			}
		}
		
		return resultadoValidar;
	}
	
	/**
	 * Método que realiza la actualización de los atributos de un objeto del tipo Unidad de Venta
	 * @param unidadVenta	Recibe un objeto del tipo Unidad de Venta
	 * @return	Devuelve 0 si se ha actualizado correctamente
	 * 			4 si ha habido un fallo durante la actualización
	 * 			5 si no se ha podido recuperar el objeto de la BBDD para actualizarlo
	 * 			variable resultadoValidar del método validarIntegridadDatos con diferentes valores. 
	 */
	public int actualizarUnidadVenta(UnidadVenta unidadVenta) {
		
		int resultadoValidar = validarIntregidadDatos(unidadVenta);
	
		if (resultadoValidar == 0 || resultadoValidar == 3) {
			
			Optional<UnidadVenta> optUnidadVenta = Optional.of(obtenerUnidadVenta(unidadVenta.getIdUnidadVenta()));
			
			if (optUnidadVenta.isPresent()) {
				
				unidadVenta.setIdUnidadVenta(optUnidadVenta.get().getIdUnidadVenta());
				
				Optional<UnidadVenta> auxUnidadVenta = Optional.of(unidadVentaDao.save(unidadVenta));
				
				if (auxUnidadVenta.isPresent()) {
					
					return 0; //Persistido correctamente
					
				}else {
					
					return 4; //Ha habido en fallo en la actualizacion del objeto
					
				}
				
			}else {
				
				return 5; //Ha habido un fallo en la obtención del objeto a ser actualizado
			}
		}
		
		return resultadoValidar;
	}
	
	/**
	 * Método que elimina un objeto del tipo Unidad de Venta
	 * @param idUnidadVenta	Recibe el Id de la Unidad de Venta a eliminar de la BBDD
	 * @return	Devuelve 0 si se ha eliminado correctamente
	 * 			1 si el Id que nos pasan es cero
	 * 			2 si el Id que nos pasan no se encuentra en la BBDD
	 */
	public int eliminaUnidadVenta(int idUnidadVenta) {
		
		if (idUnidadVenta != 0) {
			
			if (buscarUnidadVenta(idUnidadVenta)) {
				
				unidadVentaDao.deleteById(idUnidadVenta);
				
				return 0;
			}else {
				
				return 1; //No lo encuentra
			}
		}
		
		return 2; //El Id es cero
		
	}
	
	/**
	 * Método que obtiene un objeto del tipo Unidad de Venta
	 * @param idUnidadVenta Recibe el Id de la Unidad de Venta a recuperar
	 * @return	Devuelve el objeto Unidad de Venta solicitado o null sino lo consigue
	 */
	public UnidadVenta obtenerUnidadVenta(int idUnidadVenta) {
		
		if (idUnidadVenta != 0) {
			
			if (buscarUnidadVenta(idUnidadVenta)) {
				
				Optional<UnidadVenta> optUnidadVenta = unidadVentaDao.findById(idUnidadVenta);
				
				if (optUnidadVenta.isPresent()) {
					
					return optUnidadVenta.get();
				}
			}
		}
		
		return null;
	}
	
	
	/**
	 * Método que verifica si un objeto del tipo Unidad de Venta existe
	 * @param idUnidadVenta	Recibe el id de la Unidad de Ventas
	 * @return	Devuelve true si lo consigue y false sino
	 */
	public boolean buscarUnidadVenta(int idUnidadVenta) {
		
		if (idUnidadVenta != 0) {
			
			Optional<UnidadVenta> optUnidadVenta = unidadVentaDao.findById(idUnidadVenta);
			
			if (optUnidadVenta.isPresent()) {
				
				return true;
			}
			
		}
		
		return false;
	}
	
	/**
	 * Método que lista todas las Unidades de Ventas de Productos
	 * @return Una lista con todas la unidades de venta
	 */
	public List<UnidadVenta>listarTodasUnidadesVenta() {
		
		return unidadVentaDao.findAll();
	}
	
	/**
	 * Método que valida que los atributos del objeto Unidad de Venta sean correctos
	 * @param unidadVenta	Recibe un objeto del tipo Unidad de Venta
	 * @return	Devuelve 0 si los datos son correctos
	 * 			1 si el Id es ceo
	 * 			2 si la descripcion es nula
	 * 			3 si ya existe previamente el objeto en la BBDD
	 */
	private int validarIntregidadDatos(UnidadVenta unidadVenta) {
		
		if (unidadVenta.getIdUnidadVenta() != 0) {
			
			if (!buscarUnidadVenta(unidadVenta.getIdUnidadVenta())) {
				
				if ((unidadVenta.getDescripcion() != null) || (!unidadVenta.getDescripcion().isEmpty())){
					
					return 0;
					
				}else {
					
					return 2; //La descripcion es nula
					
				}
				
			}else {
				
				return 3; //Ya existe una Unidad de Venta en la BBDD
			}
			
		}else {
			
			return 1; //El Id es cero
		}
		
	}
}
