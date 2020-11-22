package org.springframework.samples.petclinic.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "trabajadores")
public class Trabajador extends BaseEntity {
	
	// Falta añadir restricciones simples y poner  DNI como clave primaria en lugar de extender a BaseEntity y tener un id.
	@Column(name = "DNI")
	@NotEmpty
	private String DNI;
	
	@Column(name = "nombre")
	@NotEmpty
	private  String nombre;
	
	@Column(name = "apellidos")
	@NotEmpty
	private  String apellidos;

	@Column(name = "correo_Electronico")
	@NotEmpty
	private  String correoElectronico;
	
	@Column(name = "telefono")
	@Digits(fraction = 0, integer = 10)
	@NotEmpty
	private  Integer telefono;
	
	@Column(name = "contraseña")
	@NotEmpty
	private  String contraseña;
	
	
	
	
//	@ManyToOne
//	@JoinColumn(name = "tipo_trabajador_id",referencedColumnName="id")
//	private  TipoTrabajador tipoTrabajador;
	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajador") //Este mappedBy no lo entiendo
//	private Set<Automovil> automoviles;

	



	@Override
	public String toString() {
		return "Trabajador [DNI=" + DNI + ", nombre=" + nombre + ", apellidos=" + apellidos + ", correoElectronico="
				+ correoElectronico + ", telefono=" + telefono + ", contraseña=" + contraseña + "]";
	}




	public String getDNI() {
		return DNI;
	}




	public void setDNI(String dNI) {
		DNI = dNI;
	}




	public String getNombre() {
		return nombre;
	}




	public void setNombre(String nombre) {
		this.nombre = nombre;
	}




	public String getApellidos() {
		return apellidos;
	}




	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}




	public String getCorreoElectronico() {
		return correoElectronico;
	}




	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}




	public Integer getTelefono() {
		return telefono;
	}




	public void setTelefono(Integer telefono) {
		this.telefono = telefono;
	}




	public String getContraseña() {
		return contraseña;
	}




	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	
	
	
}
