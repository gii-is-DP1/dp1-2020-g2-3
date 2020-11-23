package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "talleres")
public class Taller extends NamedEntity{
	
	@Column(name = "ubicacion")
	@NotEmpty
	private String ubicacion;
	
	@Column(name = "telefono")
	@Digits(fraction = 0, integer = 10)
	@NotNull
	private  Integer telefono;

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Integer getTelefono() {
		return telefono;
	}

	public void setTelefono(Integer telefono) {
		this.telefono = telefono;
	}

	@Override
	public String toString() {
		return "Taller [ubicacion=" + ubicacion + ", telefono=" + telefono + "]";
	}

	
	
	
	

}
