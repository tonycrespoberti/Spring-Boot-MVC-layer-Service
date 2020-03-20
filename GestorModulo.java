/*
 * Author: Tony Crespo - tonycrespo@outlook.com
 * System Engineer, Java Spring MVC, Data, Boot, Cloud Developer
 */
package com.myapp.bricolaje.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.bricolaje.model.persistency.IModuloDao;
import com.myapp.bricolaje.model.repo.Modulo;

@Service
public class GestorModulo {
	
	@Autowired
	private IModuloDao moduloDao;

	//------------------
	
	
	/**
	 * Método que permite dar de alta a un nuevo Modulo de la app
	 * @param modulo Recibe el objeto Modulo
	 * @return	Devuelve 0 si se ha persistido correctamento el objeto Modulo
	 * 			Devuelve 1 si no se ha especificado el nombre del modulo
	 * 			Devuelve 2 si ha habido un fallo intentando persistir el nuevo objeto Modulo
	 * 			Devuelve 3 si ya existe previamente un Modulo igual
	 */
	public int agregarModulo(Modulo modulo) {
		
		if (buscarModulo(modulo.getIdModulo())) {

			return 3; //Ya existe un modulo igual

		}else {

			if ((modulo.getModulo() != null) || (!modulo.getModulo().isEmpty())){

				Optional<Modulo> optModulo = Optional.of(moduloDao.save(modulo));

				if (optModulo.isPresent()) {

					return 0; //Modulo persistido correctamente

				}else {

					return 2; //Ha habido un fallo durante la persistencia
				}

			}else {

				return 1; //El nombre del modulo no se ha especificado
			}
		}
	}
	
	
	/**
	 * Método que actualizado un objeto del tipo Modulo
	 * @param 	modulo Recibe un objeto de tipo Modulo
	 * @return	0 si ha sido actualizado correctamente
	 * 			1 si no se ha suministrado Id
	 * 			2 si no se suministrado una descripcion
	 * 			3 si ha habido un fallo durante la persistencia
	 */
	public int modificarModulo(Modulo modulo) {
		
		if (modulo.getIdModulo() != 0) {
			
			if (modulo.getModulo() != null) {
				
				Optional<Modulo> optModulo = moduloDao.findById(modulo.getIdModulo());
				
				if (optModulo.isPresent()) {
					
					modulo.setIdModulo(optModulo.get().getIdModulo());
					
					Optional<Modulo> auxModulo = Optional.of(moduloDao.save(modulo));
					
					if (auxModulo.isPresent()) {
						
						return 0; //Actualizado correctamente
						
					}else {
						
						return 3; //Ha habido un fallo durante la persistencia
						
					}
					
				}
				
			}else {
				
				return 2; //La descripcion del módulo es null
			}
			
		}else {
			
			return 1; //El id del módulo es cero
		}
		
		
		return 0;
	}
	
	
	/**
	 * Método que busca si existe un Modulo
	 * @param idModulo Recibe el id del modulo a buscar
	 * @return Devuelve true si lo encuentra y false sino.
	 */
	public boolean buscarModulo(int idModulo) {
		
		Optional<Modulo> optModulo = moduloDao.findById(idModulo);

		if (optModulo.isPresent()) {
			
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Método que elimina un módulo de la BBDD
	 * @param idModulo Recibe el id del modulo a eliminar
	 * @return Devuelve true si ha sido eliminado correctamente y false sino
	 */
	public boolean eliminarModulo(int idModulo) {
		
		if (idModulo != 0) {
			
			moduloDao.deleteById(idModulo);
			
			return true;
			
			
		}else {
			
			return false;
			
		}
	}
	
	
	/**
	 * Método que lista todos los Modulos de la app
	 * @return lista con todos los objetos de tipo Modulo
	 */
	public List<Modulo> listarModulos(){
		
		return moduloDao.findAll();
	}
}
