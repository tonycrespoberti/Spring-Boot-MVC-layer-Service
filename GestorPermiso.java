/*
 * Author: Tony Crespo - tonycrespo@outlook.com
 * System Engineer, Java Spring MVC, Data, Boot, Cloud Developer
 */
package com.myapp.bricolaje.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.bricolaje.model.persistency.IPermisoDao;
import com.myapp.bricolaje.model.repo.Permiso;

@Service
public class GestorPermiso {
	
	@Autowired
	private IPermisoDao permisoDao;
	
	//---------------------------
	
	
	/**
	 * Método que da de Alta a nuevo Permisos
	 * @param permiso Recibe un objeto del tipo Permiso
	 * @return	Devuelve 0 si se ha agregado correctamente
	 * 			4 si ha habido un fallo en el proceso de persistencia del nuevo objeto
	 * 			la variable resultadoValidar del método validarIntegridadDatos con diversos códigos de error
	 */
	public int agregarPermiso(Permiso permiso) {
		
		int resultadoValidar = validaIntegridadDatos(permiso);
		
		if (resultadoValidar == 0 ||  resultadoValidar == 1) {
			
			Optional<Permiso> optPermiso = Optional.of(permisoDao.save(permiso));
			
			if (optPermiso.isPresent()) {
				
				return 0;
				
			}else {
				
				return 4; //Ha habido un fallo en la persistencia del nuevo objeto
			}
		}
		
		return resultadoValidar;
	}
	
	/**
	 * Método que elimina un permiso
	 * @param idPermiso Recibe el id del permiso
	 * @return	Devuelve true si lo ha eliminado correctamente o false si se ha recibido un Id incorrecto
	 */
	public boolean eliminarPermiso(int idPermiso) {
		
		if (idPermiso != 0) {
			
			permisoDao.deleteById(idPermiso);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Método que actualiza los atributos del objeto Permiso
	 * @param permiso	Recibe un objeto del tipo permiso
	 * @return	Devulve 0 si lo ha actualizado correctamente
	 * 			Devuelve la variable resultadoValidar del metodo validarIntegridadDatos() con diversos valores.
	 */
	public int actualizarPermiso(Permiso permiso) {
		
		int resultadoValidar = validaIntegridadDatos(permiso);
		
		if (resultadoValidar == 0) {
			
			Optional<Permiso> optPermiso = Optional.of(obtenerPermiso(permiso.getIdPermiso()));
			
			if (optPermiso.isPresent()){
				
				permiso.setIdPermiso(optPermiso.get().getIdPermiso());
				
				permisoDao.save(permiso);
				
				return 0;
			}
		}
		
		return resultadoValidar;
	}
	
	/**
	 * Método que obtiene un objeto del tipo Permiso
	 * @param idPermiso Recibe un objeto del tipo Permiso
	 * @return	Devuelve el objeto Permiso si es encontrado
	 */
	public Permiso obtenerPermiso(int idPermiso) {
		
		if (idPermiso != 0) {
			
			if (buscarPermiso(idPermiso)) {
				
				Optional<Permiso> optPermiso = permisoDao.findById(idPermiso);
				
				return optPermiso.orElse(null);
			}
		}
		
		return null;
	}
	
	/**
	 * Método que verifica si existe un objeto del tipo Permiso especificado
	 * @param idPermiso Recibe el id del objeto Permiso
	 * @return	Devuelve true si lo consigue y false sino
	 */
	public boolean buscarPermiso(int idPermiso) {
		
		if (idPermiso !=0 ) {
			
			Optional<Permiso> optPermiso = permisoDao.findById(idPermiso);
			
			if (optPermiso.isPresent()) {
			
				return true;
			}
		}
		
		return false;
		
	}
	
	/**
	 * Método que lista todos los permisos existentes
	 * @return Devuelve una lista con todos los permisos
	 */
	public List<Permiso> listarTodosPermisos(){
		
		return permisoDao.findAll();
	}
	
	/**
	 * Método que valida que los datos del objeto Permiso sean correctos
	 * @param permiso Recibe un objeto del tipo Permiso
	 * @return	Devuelve 0 si los atributos son correctos
	 * 			1 si el id del permiso es cero
	 * 			2 si la descripcion del permiso es null o blanco
	 * 			3 si existe un permiso ya registrado
	 */
	public int validaIntegridadDatos(Permiso permiso) {
		
		if (permiso.getIdPermiso() != 0) {
			
			if (!buscarPermiso(permiso.getIdPermiso())) {
			
				if ((permiso.getTipoPermiso() != null) || (!permiso.getTipoPermiso().isEmpty())){
					
					return 0;
					
				}else {
					
					return 2; //La descripcion del permiso es null or dejado en blanco
				}
			}else {
				
				return 3; //Existe el permiso
				
			}
				
		}else {
			
			return 1; //El Id es igual a cero
		}
	}

}
