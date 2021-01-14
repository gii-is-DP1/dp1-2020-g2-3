package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "contratos")
public class Contrato extends BaseEntity{

	@Column(name = "salario_mensual")
	@NotNull
	private Double salarioMensual;
	
	@Column(name = "fecha_inicio")
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate fechaInicio;
	
	@Column(name = "fecha_fin")
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate fechaFin;
	
	@ManyToOne
	@JoinColumn(name = "trabajador_id",referencedColumnName="id") //Cuando la clave primaria de Trabajador sea DNI habrá que referenciarlo aquí en lugar del id como está ahora.
	private Trabajador trabajadorId;

	public Double getSalarioMensual() {
		return salarioMensual;
	}

	public void setSalarioMensual(Double salarioMensual) {
		this.salarioMensual = salarioMensual;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Trabajador getTrabajadorId() {
		return trabajadorId;
	}

	public void setTrabajador(Trabajador trabajadorId) {
		this.trabajadorId = trabajadorId;
	}

	@Override
	public String toString() {
		return "Contrato [salarioMensual=" + salarioMensual + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin
				+ ", trabajador=" + trabajadorId + "]";
	}
	
	
}
