/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Simple JavaBean domain object representing an owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
@Entity
@Table(name = "servicios")
public class Servicio extends NamedEntity{

	@Column(name = "fecha")
	@NotEmpty
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate fecha;
	
	@Column(name = "precio")
	@NotEmpty
	private Double precio;

	@ManyToOne
	@JoinColumn(name = "trabajador_id",referencedColumnName="id") //Cuando la clave primaria de Trabajador sea DNI habrá que referenciarlo aquí en lugar del id como está ahora.
	private Trabajador trabajador;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "automovil_id", referencedColumnName = "id")
	private Automovil automovil;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "talleres_id", referencedColumnName = "id")
	private Taller taller;
	
	@Column(name = "descripcion")
	@NotEmpty
	private String descripcion;

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Trabajador getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}

	public Automovil getAutomovil() {
		return automovil;
	}

	public void setAutomovil(Automovil automovil) {
		this.automovil = automovil;
	}

	public Taller getTaller() {
		return taller;
	}

	public void setTaller(Taller taller) {
		this.taller = taller;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "Servicio [fecha=" + fecha + ", precio=" + precio + ", trabajador=" + trabajador + ", automovil="
				+ automovil + ", taller=" + taller + ", descripcion=" + descripcion + "]";
	}
	
	
	

}
