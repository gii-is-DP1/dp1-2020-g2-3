package org.springframework.samples.petclinic.model;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
@Data
@Entity
@Table(name = "Trabajador")
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
	
	
	@ManyToOne
	@JoinColumn(name = "tipo_trabajador_id",referencedColumnName="id")
	private  TipoTrabajador tipoTrabajador;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajador") //Este mappedBy no lo entiendo
	private Set<Automovil> automoviles;

	@Override
	public String toString() {
		return "Trabajador [DNI=" + DNI + ", nombre=" + nombre + ", apellidos=" + apellidos + ", correoElectronico="
				+ correoElectronico + ", telefono=" + telefono + ", contraseña=" + contraseña + ", tipoTrabajador="
				+ tipoTrabajador + ", automoviles=" + automoviles + "]";
	}
	
	
	
}
