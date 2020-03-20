/*
 * Author: Tony Crespo - tonycrespo@outlook.com
 * System Engineer, Java Spring MVC, Data, Boot, Cloud Developer
 */
package com.myapp.bricolaje.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.bricolaje.model.persistency.IDireccionDao;
import com.myapp.bricolaje.model.repo.Direccion;

@Service
public class GestorDireccion {

	@Autowired
	private IDireccionDao direccionDao;


	//-------------------
	
	/**
	 * Método que agrega nuevas direcciones a la BBDD
	 * @param direccion Recibe el objeto Direccion para ser validado 
	 * @return	0 si fue añadido correctamente
	 * 			1 si el id es cero
	 * 			2 si la calle/avenida es null
	 * 			3 si el numero es null
	 * 			4 si la planta es null
	 * 			5 si la puerta es null
	 * 			6 si la localidad es null
	 * 			7 si la provincia es null
	 * 			8 si el id ya existe previamente
	 * 			9 si el codigo postal es cero
	 * 			10 si el tipo de direccion es cero
	 * 			11 Ha habido un fallo intentando persistir la nueva direccion en la BBDD
	 */
	public int agregarDireccion(Direccion direccion) {
		
		int resultadoValidacion = validarIntegridadDatos(direccion);
		
		if (resultadoValidacion == 0) {
	
			Optional<Direccion> optDireccion = Optional.of(direccionDao.save(direccion));

			if (optDireccion.isPresent()){

				return 0; //Se ha persistido la nueva direccion correctamente

			}else {

				return 11; //Fallo de la conexión o la BBDD
			}
		}
		
		return resultadoValidacion;
	}
	
	
	/**
	 * Método para actualizar un objeto del tipo Direccion
	 * @param direccion Recibe el objeto direccion a modificar
	 * @return 	Devuelve 0 si fue actualizado correctamente
	 * 			Devuelve 1 si no lo encuentra para su actualización
	 * 			Devuelve resultadoValidacion conjunto de error producto de la validacion de los atributos del objeto Direccion 
	 * 			suministrado por parámetro
	 */
	public int actualizarDireccion(Direccion direccion) {
		
		int resultadoValidacion = validarIntegridadDatos(direccion);
		
		if (resultadoValidacion == 0) {
			
			Optional<Direccion> optDireccion = direccionDao.findById(direccion.getIdDireccion());
			
			if (optDireccion.isPresent()) {
				
				direccion.setIdDireccion(optDireccion.get().getIdDireccion());
				
				direccionDao.save(direccion);
				
				return 0;
				
			}else {
				
				return 1; //No se encuentra el objeto solicitado
				
			}
		}
		
		return resultadoValidacion;
	}
	
	
	/**
	 * Método que busca si exsite una Direccion
	 * @param idDireccion Recibe el id de la dirección a buscar
	 * @return Devuelve true si la consige y false si no existe
	 */
	public boolean buscarDireccion(int idDireccion) {
		
		Optional<Direccion> optDireccion = direccionDao.findById(idDireccion);
		
		if (optDireccion.isPresent()){
			
			return true;
			
		}
		return false;
	}
	
	
	/**
	 * Método que busca si una direccion existe
	 * @param idDireccion Recibe el id de la Direccion
	 * @return 0 si lo ha eliminado correctamente
	 * 			1 si el id recibido es cero
	 * 			2 si el id recibuido no se existe BBDD
	 */
	public int eliminarDireccion(int idDireccion) {
		
		if (idDireccion != 0) {
			
			//Buscamos antes si el id existe 
			if (buscarDireccion(idDireccion)) {
		
				direccionDao.deleteById(idDireccion);
			
				return 0;
			
			}else {
			
				return 1; //El id no puede ser cero
			}
		}else {
			
			return 2; //No encuentra el id suministrado
		}
	}
	
	
	/**
	 * Método que lista todas las direcciones
	 * @return Una lista de direcciones
	 */
	public List<Direccion> listarTodasDirecciones() {
		
		return direccionDao.findAll();
	}
	
	
	/**
	 * Método que lista todas las direcciones por calle/avenida
	 * @return Una lista de direcciones
	 */
	public List<Direccion> listarDireccionesPorCalleAvenida(String calleAvenida) {
		
		return direccionDao.findByAllCalleAvenida(calleAvenida);
	}
	
	
	/**
	 * Método que una direccion específica por calle/avenida
	 * @return Una lista de direcciones que coincidan con la calle/avenida especificada
	 */
	public Optional<Direccion> obtenerDireccionPorCalleAvenida(String calleAvenida) {
		
		return direccionDao.findByCalleAvenida(calleAvenida);
	}
	
	
	/**
	 * Método que devuelve una lista de direcciones por código postal
	 * @param codigoPostal Recibe un código postal
	 * @return Devuelve una lista de direcciones según el código postal
	 */
	public List<Direccion> listarDireccionesPorCodigoPostal(int codigoPostal){
		
		return direccionDao.findByCodigoPostal(codigoPostal);
	}
	
	
	public List<Direccion> listarDireccionesPorLocalidad(String localidad){
		
		return direccionDao.findByLocalidad(localidad);
	}
	
	
	public List<Direccion> listarDirecciionesPorProvincia(String provincia){
		
		return direccionDao.findByProvincia(provincia);
	}
	
	
	/**
	 * Método que valida los atributos recibidos de una Direccion
	 * @param direccion Recibe el objeto Direccion para ser validado 
	 * @return	0 el objeto ha sido validado
	 * 			1 si el id es cero
	 * 			2 si la calle/avenida es null
	 * 			3 si el numero es null
	 * 			4 si la planta es null
	 * 			5 si la puerta es null
	 * 			6 si la localidad es null
	 * 			7 si la provincia es null
	 * 			8 si el id ya existe previamente
	 * 			9 si el codigo postal es cero
	 * 			10 si el tipo de direccion es cero
	 */
	private int validarIntegridadDatos(Direccion direccion) {
	
		//Procedemos a validar el objeto antes de persistirlo.

		if (direccion.getIdDireccion() == 0) {

			return 1; //El id es cero

		}

		if (buscarDireccion(direccion.getIdDireccion())) {

			return 8; //Ya existe un id previo

		}

		if ((direccion.getCalleAvenida() != null) || (!direccion.getCalleAvenida().isEmpty())) {

			if (direccion.getNumero() != null) {

				if ((direccion.getPlanta() != null) || (!direccion.getPlanta().isEmpty())) {

					if ((direccion.getPuerta() != null) || (!direccion.getPuerta().isEmpty())) {

						if((direccion.getLocalidad() != null) || (!direccion.getLocalidad().isEmpty())){

							if ((direccion.getProvincia() != null) || (!direccion.getProvincia().isEmpty())){

								if (direccion.getCodigoPostal() != 0) {

									if(direccion.getTipoDireccion().getIdTipoDireccion() != 0) {

										return 0;

									}else {

										return 10; //El id del Tipo de Direccion es cero
									}

								}else {

									return 9; //El codigo postal es cero
								}

							}else {

								return 7; //La provincia es null
							}

						}else {

							return 6; //La localidad es null
						}

					}else {

						return 5; //La puerta es null
					}

				}else {

					return 4; //La planta es null
				}

			}else {

				return 3; //El numero es null
			}

		}else {

			return 2; // Calle/Avenido es null

		}

	}
}
