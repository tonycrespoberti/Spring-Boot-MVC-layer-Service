/*
 * Author: Tony Crespo - tonycrespo@outlook.com
 * System Engineer, Java Spring MVC, Data, Boot, Cloud Developer
 */
package com.myapp.bricolaje.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.bricolaje.model.persistency.IUsuarioDao;
import com.myapp.bricolaje.model.repo.Usuario;

@Service
public class GestorUsuario {

	@Autowired
	private IUsuarioDao usuarioDao;
	
	//------------------
	
	/**
	 * Método que da de Alta a nuevos Usuarios
	 * @param usuario Recibe el objeto Usuario
	 * @return	0 Devuelve 0 si fue agregado el nuevo Usuario
	 * 			9 Si ha habido un fallo en la persistencia
	 * 			resultadoValidacion conjunto de códigos generados propios de la valicacion de cada atributo del objeto Usuario
	 * 			Ver método validarIntegridadDatos()
	 */
	public int altaUsuario(Usuario usuario) {
		
		int resultadoValidacion = validarIntegridadDatos(usuario);
		
		if ( resultadoValidacion == 0) {
			
			Optional<Usuario> optUsuario = Optional.of(usuarioDao.save(usuario));
			
			if (optUsuario.isPresent()) {
				
				return 0; //Nuevo Usuario registrado correctamente
			}else {
			
				return 9; // Si ha habido un fallo persistiendo el objeto Usuario
				
			}
		}
		
		return resultadoValidacion; //Devueleve el código por error en la validacion de los datos recibidos
		
	}
	
	/**
	 * Método que verifica la existencia de un Usuario
	 * @param idUsuario El id del Usuario
	 * @param dni El documento de identidad del Usuario
	 * @return Devuelve true si lo encuentra y false si no.
	 */
	public boolean buscarUsuario(String dni) {
		
		Optional<Usuario> optUsuario = Optional.of(usuarioDao.findByDni( dni));
		
		if (optUsuario.isPresent()){
			
			return true; //Encuentra un Usuario en la BBDD con esos datos
		}
		
		return false;
	}
	
	
	/**
	 * Método que busca y obtiene un objeto del tipo Usuario por medio del DNI
	 * @param dni	Recive el DNI del Usuario como parámetro
	 * @return	Devuelve el objeto Usuario encontrado, sino un null
	 */
	public Usuario obtenerUsuario(String dni) {
		
		if (dni != null) {
			
			Optional<Usuario> optUsuario = Optional.of(usuarioDao.findByDni(dni));
			
			return optUsuario.orElse(null); //Si no hay objeto de vuelta envía null
			
		}else {
			
			return null;
		}
	}
	
	
	/**
	 * Método que modifica los atributos del objeto Usuario
	 * @param usuario	Recibe el nuevo objeto Usuario a ser actualizado
	 * @return	Devuelve 0 si el cambio ha sido persistido correctamente
	 * 			Devuelve la variable resultadoValidacion para advertir si falta algun atributo del objeto por definir
	 * 			Devuelve 12 si ha habido problemas actualizando el objeto en la BBDD
	 */
	public int modificarUsuario(Usuario usuario) {
		
		int resultadoValidacion = validarIntegridadDatos(usuario);
		
		if (resultadoValidacion == 0) {
			
			Optional<Usuario> optUsuario = Optional.of(usuarioDao.findByIdUsuarioAndDni(usuario.getIdUsuario(), usuario.getDni()));
			
			//Recuperamos el Id para asignarlo y pasandolo al método save se entiende que es una actualización y no una operacion de nuevo Usuario
			//incrementando el Id
			usuario.setIdUsuario(optUsuario.get().getIdUsuario());
			
			if (optUsuario.isPresent()) {
				
				usuarioDao.save(usuario);
				
			}else {
				
				return 12; //Ha habido un fallo intentando actualizar el objeto en la BBDD
			}
		}
		
		return resultadoValidacion;
	}
	
	
	/**
	 * Método para dar de baja a Usuario
	 * @param dni Recibe el DNI del usuario
	 * @return
	 */
	public int eliminarUSuario(String dni) {

		if (dni != null) {

			Optional<Usuario> optUusuario = Optional.of(usuarioDao.findByDni(dni));

			if (optUusuario.isPresent()) {

				usuarioDao.delete(optUusuario.get());

				return 0; // La eliminacion ha sido completada

			} else {

				return 3; // Error intentando eliminar el objeto Usuario
			}

		} else {

			return 2; // El Dni es nulo
		}
	}
	
	
	/**
	 * Método para buscar todos los Usuarios que coincidan con el parámetro suministrado
	 * @param nombres Recibe el nombre que será la coincidencia de la búsqueda
	 * @return Devuelve la lista de Usuarios cuyos nombres coincidan
	 */
	public List<Usuario> buscarUsuarioPorNombre(String nombres){
		
		return usuarioDao.findByNombres(nombres);
	}
	
	
	/**
	 * Método que proporciona una lista de todos los usuarios existentes
	 * @return Devuelve una lista de objetos tipo Usuario
	 */
	public List<Usuario> listarUsuarios(){
		
		return usuarioDao.findAll();
	}
	
	/**
	 * Método utilizado para validar que los datos recibidos del Usuario son los esperados
	 * @param usuario Recibe como parámetro un objeto del tipo Usuario
	 * @return	Devuelve 0 si la persistencia fue exitosa
	 * 			2 si el DNI es nulo
	 * 			3 si el usuario ya existe
	 * 			4 si el Nombre es nulo
	 * 			5 si el Apellido es nulo
	 * 			6 si la Edad es cero
	 * 			7 si el eMail es nulo
	 * 			8 si el Telefono es cero
	 * 			10 Si no hay Rol definido
	 * 			11 Si no tiene direccion asignada
	 */			
	private int validarIntegridadDatos(Usuario usuario) {
		
		if (usuario.getDni() != null) {

			if (buscarUsuario(usuario.getDni())) {

				return 3; //El usuario ya existe

			}else {

				if (usuario.getNombres() != null) {

					if (usuario.getApellidos() != null) {

						if (usuario.getEdad() != 0) {

							if (usuario.getEmail() != null) {

								if (usuario.getTelefono() != 0) {
									
									if (usuario.getRol().getIdRol() != 0) {
										
										if(usuario.getDireccion().getIdDireccion() != 0) {
											
											return 0; //La validación de todos los datos del Usuario ha sido correcta
											
										}else {
											
											return 11; //No tiene direcciones definida
										}
										
									}else {
										
										return 10; //No hay rol establecido
									}

								}else {

									return 8; //El telefono es cero

								}

							}else {

								return 7; //El email es null

							}

						}else {

							return 6; //La edad es cero

						}

					}else {

						return 5; //El apellido es null

					}

				}else {

					return 4; //El nombre es null
				}
			}

		}else {

			return 2; // El dni es null

		}
	}
}
