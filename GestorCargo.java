/*
 * Author: Tony Crespo - tonycrespo@outlook.com
 * System Engineer, Java Spring MVC, Data, Boot, Cloud Developer
 */
package com.myapp.bricolaje.model.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.bricolaje.model.persistency.ICargoDao;
import com.myapp.bricolaje.model.repo.Cargo;

@Service
public class GestorCargo {

	@Autowired
	private ICargoDao cargoDao;

	
	//--------------
	
	/**
	 * Alta de Cargo. Entidad que persiste los tipos de cargo: Supervidor, Empleado, Intern, VicePresid, Director, Gerente
	 * @param cargo Recibe un objeto del tipo Cargo
	 * @return 	0 si todo Ok, 
	 * 			1 sino hay especificado un Id, 
	 * 			2 sino hay descripcion, 
	 * 			3 si hubo problemas en persistirlo, 
	 * 			4 si ya existe uno previamente en la BBDD
	 */
	@Transactional
	public int agregarCargo(Cargo cargo) {
		//Procedemos a validar el objeto antes de persistirlo.
		
		if (cargo.getIdCargo() == 0) {
				
			return 1;
			
		}else if ((cargo.getDescripcion() != null) || (!cargo.getDescripcion().isEmpty())) {	
			
			//Antes de persistir el nuevo objeto, verificamos si no existe previamente		
			if (buscarCargo(cargo.getIdCargo())) {
				
				return 4; //Ya existe un Cargo igual en la BBDD
				
			}else {
			
				//Para estar seguros de que la persistencia fue completada correctamente dado que no tenemos forma
				//de asegurar que no ha habido un fallo en la conexión o la BBDD ha generado un error.
				Optional<Cargo> optCargo = Optional.of(cargoDao.save(cargo));
				
				if (optCargo.isPresent()) {
					
					return 0; //Persistido correctamente
				
				}else {
					
					return 3; //Ha habido un fallo en la persistencia del cargo
				}
			}
			
		} else {
			
			return 2; // Descripcion es null
		}
	}
	
	/**
	 * Método que verifica si existe un Cargo
	 * @param idCargo Recibe el id del cargo para su búsqueda
	 * @return Devuelve true si existe y false sino lo encuentra en la BBDD.
	 */
	@Transactional
	public boolean buscarCargo(int idCargo) {
	
		Optional<Cargo> optCargo = cargoDao.findById(idCargo);
		
		//Evaluamos el objeto si tiene contendio
		if (optCargo.isPresent()){
			
			return true; //Encuentra el id
			
		}
		
		return false; //No consigue el id suministrado
	}
	
	
	/**
	 * Método para recuperar un objeto del tipo Cargo
	 * @param idCargo	Recibe el Id del Cargo
	 * @return	Devuelve el objeto Cargo si lo encuentra, sino null
	 */
	public Cargo obtenerCargo(int idCargo) {
		
		if (idCargo != 0) {
			
			Optional<Cargo> optCargo = cargoDao.findById(idCargo);
			
			if (optCargo.isPresent()) {
				
				return optCargo.orElse(null);
				
			}
			
		}
		
		return null;
	}
	
	
	public Cargo obtenerCargoPorDescripcion(String descripcion) {
		
		if (descripcion != null) {
			
			Optional<Cargo> optCargo = Optional.of(cargoDao.findByDescripcion(descripcion));
			
			return optCargo.orElse(null);
		}
		
		return null;
			
	}
	
	
	/**
	 * Método que elimina un cargo de la BBDD
	 * @param idCargo Recibe el id del cargo
	 * @return 	Devuelve 0 si es eliminado correctamente
	 * 			Devuelve 1 si hemos recibido un id con valor cero.
	 * 			Devuelve 2 si no encuentra en la BBDD el id suministrado.
	 */
	@Transactional
	public int eliminarCargo(int idCargo) {
		
		if (idCargo != 0) {
			
			//Buscamos antes si el id existe 
			if (buscarCargo(idCargo)) {
		
				cargoDao.deleteById(idCargo);
			
				return 0;
			
			}else {
			
				return 1; //El id no puede ser cero
			}
		}else {
			
			return 2; //No encuentra el id suministrado
		}
	}
	
		
	/**
	 * Método para modificar atributo descripcion del objeto Cargo
	 * @param cargo Se recibo el objeto Cargo completo ya que debemos pasar el id y su descripcion al invocar el método Save para que haga
	 * la actualizacion. Si pasamos solo la descripcion, el id que es autoincremnet en la BBDD creará un nuevo Cargo con descripción repetida.
	 * @return 	Devuelvo cero si fue OK
	 * 			1 si el id recibido es cero
	 * 			2 si no encuentra el id en la BBDD
	 * 			3 si ha habido un fallo intentando persistir y efectual el update del objeto
	 */
	@Transactional
	public int modificarCargo(Cargo cargo) {
		
		//Validamos antes que existe y que lo encuentra antes de modificarlo
		if (cargo.getIdCargo() != 0) {
			
			if(buscarCargo(cargo.getIdCargo())) {
				
				//Al pasar el objeto con su Id el objeto es actualizado en la BBDD.
				Optional<Cargo> optCargo = Optional.of(cargoDao.save(cargo));
				
				if (optCargo.isPresent()) {
					
					return 0; //modificado correctamente
				
				}else {
					
					return 3; //Ha habido un fallo en la actualizacion del objeto cargo
				}
				
			}else {
				
				return 2; //No encuentra el id en la BBDD
			}
			
		}else {
			
			return 1; //El id recibido es cero.
		}
	}
	
	/**
	 * Método que devuelve una lista de cargo
	 * @return Lista de Cargos
	 */
	public List<Cargo> listarCargos() {
		
		return cargoDao.findAll();
	}
	
	
	/**
	 * Método que busca todos los cargos que comiencen por la descripcion suministrada
	 * @param descripcion	Recibe la descripción por la que deben comenzar todos los cargos.
	 * @return	Lista de los cargos conseguidos que comiencen por la descripcion suministrada
	 */
	public List<Cargo> listCargosDescripcionExacta(String descripcion){
		
		return cargoDao.findByDescripcionExacta(descripcion.toUpperCase());
	}
	
	
	/**
	 * Método que proporciona una lista de todos los cargos que contengan la descripcion proporcionada
	 * @param descripcion	Recibe la descripción por la que se buscará las coincidencias
	 * @return	Retorna la lista de cargos correspondiente
	 */
	public List<Cargo> listarCargoQueContengaDescripcion(String descripcion){
		
		return cargoDao.findByQueContengaDescripcion(descripcion.toUpperCase());
	}
}
