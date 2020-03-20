/*
 * Author: Tony Crespo - tonycrespo@outlook.com
 * System Engineer, Java Spring MVC, Data, Boot, Cloud Developer
 */
package com.myapp.bricolaje.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.myapp.bricolaje.model.persistency.IRolDao;
import com.myapp.bricolaje.model.repo.Rol;

public class GestorRol {
	
	@Autowired
	private IRolDao rolDao;

	//----------------------
	
	
	/**
	 * Metodo que da de alta un nuevo Rol
	 * @param rol Recibe un objeto del tipo Rol
	 * @return	Devuelve 0 si el alta fue correcta
	 * 			3 si el objeto ya existe
	 * 			4 si ha habido un fallo intentando persistir el nuevo objeto
	 * 			resultadoValidacion, parametro devuelto a su vez por el método validarIntegridadDato. Ver este método para nuevo códigos de error.
	 */
	public int agregarRol(Rol rol) {
		
		int resultadoValidacion;
		
		resultadoValidacion = validarIntegridadDatos(rol);
		
		if (resultadoValidacion == 0) {
			
			if (buscarRol(rol.getIdRol())){
				
				return 3; //El rol ya existe
		
			}else {
			
				Optional<Rol> optRol = Optional.of(rolDao.save(rol));
				
				if (optRol.isPresent()) {
					
					return 0;
					
				}else {
					
					return 4; //Ha habido un fallo persistiendo el nuevo objeto
				}
				
			}
		}
		
		return resultadoValidacion;
	}
	
	/**
	 * Método que elimina un objeto del tipo Rol
	 * @param rol Recibe un objeto del tipo Rol
	 * @return	Devuelve 0 si fue eliminado correctamente
	 * 			Devuelve conjunto de valores asociados a la variable resultadoValidacion (ver detalle en el método validarIntegridadDatos
	 */
	public int eliminarRol(Rol rol) {
		
		int resultadoValidacion;
		
		resultadoValidacion = validarIntegridadDatos(rol);
		
		if (resultadoValidacion == 0) {
			
			rolDao.deleteById(rol.getIdRol());
			
			return 0;
		}
		
		return resultadoValidacion;
	}
	
	/**
	 * Método que obtiene un objeto del tipo Rol
	 * @param idRol Id del rol a obtener
	 * @return	un Objeto de tipo Rol o null sino lo encuentra
	 */
	public Rol obtenerRol(int idRol) {
		
		if (idRol != 0) {
			
			Optional<Rol> optRol = rolDao.findById(idRol);
			
				return optRol.orElse(null);

		}
		
		return null;
	}
	
	/**
	 * Método que busca un objeto del tipo Rol según el Id suministrado
	 * @param idRol Id del Rol a buscar
	 * @return Devuelve true si lo consigue y false sino
	 */
	public boolean buscarRol(int idRol) {
		
		if (idRol != 0) {
			
			Optional<Rol> optRol = rolDao.findById(idRol);
			
			if (optRol.isPresent()){
				
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Método que busca todos los roles existentes
	 * @return Devuelve una lista con todos los roles
	 */
	public List<Rol> listarTodosRoles() {
		
		return rolDao.findAll();
	}
	

	
	/**
	 * Metodo que valida que los datos recibidos del Rol son correcto
	 * @param rol Recibe un objeto del tipo Rol
	 * @return	0 si está OK
	 * 			1 si el Id es cro
	 * 			2 si la descripción es null
	 */
	public int validarIntegridadDatos(Rol rol) {
		
		if (rol.getIdRol() != 0) {
			
			if (rol.getDescripcion() != null) {
				
				return 0; //Datos correctos
				
			}else {
				
				return 2; //La descripcion es null
			}
			
		}else {
			
			return 1; // El id del rol es cero
		}
		
	}
}
