package org.springframework.samples.petclinic.model;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
@Data
@Entity
@Table(name = "trabajador")
public class Trabajador extends Person {
	
	
	@Column(name = "email")
	@NotEmpty
	private  String email;
	
	@Column(name = "telefono")
	@Digits(fraction = 0, integer = 10)
	@NotNull
	private  Integer telefono;
	
	
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "tipo_trabajador_id",referencedColumnName="id")
	private  TipoTrabajador tipoTrabajador;
	
	

	@Override
	public String toString() {
		return "Trabajador [ " + ", correoElectronico="
				+ email + ", telefono=" + telefono + ", "+  "]";
	}

	
	
}
