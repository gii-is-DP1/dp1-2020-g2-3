package org.springframework.samples.petclinic.model;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "reserva")
public class Reserva extends BaseEntity {
	
	@ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
	private Cliente cliente;
	
	@ManyToOne
    @JoinColumn(name = "ruta_id", referencedColumnName = "id")
	private Ruta ruta;
	
	@Column(name = "fecha_Salida")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@NotNull
	private Date fechaSalida;
	

	@Column(name = "fecha_Llegada")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date fechaLlegada;

	@Column(name = "hora_Salida")
	@NotNull
	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	private Date horaSalida;
	
	@Column(name = "hora_Llegada")
	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	private Date horaLlegada;
	
	
	@Min(0)
	@Digits(fraction=2,integer=3)
	@Column(name = "horas_Espera")
	private  Double horasEspera;
	
	
	@Column(name = "plazas_Ocupadas")
	@NotNull
	@Digits(fraction = 0, integer = 1)
	@Max(6)
	@Min(1)
	private  Integer plazas_Ocupadas;
	
	//Poner tipoPrivacidad en caso de implementar lo de compartir viajes
	

	@Column(name = "descripcion_Equipaje")
	@Size(min = 0, max =280)
	private String descripcionEquipaje;
	
	
	@ManyToOne
	@JoinColumn(name = "estado_Reserva_id",referencedColumnName="id")
	private  EstadoReserva estadoReserva;


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public Ruta getRuta() {
		return ruta;
	}


	public void setRuta(Ruta ruta) {
		this.ruta = ruta;
	}


	public Date getFechaSalida() {
		return fechaSalida;
	}


	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}


	public Date getFechaLlegada() {
		return fechaLlegada;
	}


	public void setFechaLlegada(Date fechaLlegada) {
		this.fechaLlegada = fechaLlegada;
	}


	public Date getHoraSalida() {
		return horaSalida;
	}


	public void setHoraSalida(Date horaSalida) {
		this.horaSalida = horaSalida;
	}


	public Date getHoraLlegada() {
		return horaLlegada;
	}


	public void setHoraLlegada(Date horaLlegada) {
		this.horaLlegada = horaLlegada;
	}


	public Double getHorasEspera() {
		return horasEspera;
	}


	public void setHorasEspera(Double horasEspera) {
		this.horasEspera = horasEspera;
	}


	public Integer getPlazas_Ocupadas() {
		return plazas_Ocupadas;
	}


	public void setPlazas_Ocupadas(Integer plazas_Ocupadas) {
		this.plazas_Ocupadas = plazas_Ocupadas;
	}


	public String getDescripcionEquipaje() {
		return descripcionEquipaje;
	}


	public void setDescripcionEquipaje(String descripcionEquipaje) {
		this.descripcionEquipaje = descripcionEquipaje;
	}


	public EstadoReserva getEstadoReserva() {
		return estadoReserva;
	}


	public void setEstadoReserva(EstadoReserva estadoReserva) {
		this.estadoReserva = estadoReserva;
	}
	
	//Meter relación reserva recursiva en caso de implementar compartir viajes
	@Min(0)
	@Digits(fraction=2,integer=5)
	@Column(name = "precio_Total")
	private  Double precioTotal;
	
	@Min(0)
	@Digits(fraction=2,integer=6)
	@Column(name = "num_Km_Totales")
	private  Double numKmTotales;
	
	@Min(0)
	@Digits(fraction=2,integer=5)
	@Column(name = "precio_Distancia")
	private  Double precioDistancia;
	
	
	@Min(0)
	@Digits(fraction=2,integer=5)
	@Column(name = "precio_Espera")
	private  Double precioEspera;
	
	@Min(0)
	@Digits(fraction=2,integer=5)
	@Column(name = "precio_IVA_Repercutivo")
	private  Double precioIVA;
	
	@Min(0)
	@Digits(fraction=2,integer=5)
	@Column(name = "base_Imponible")
	private  Double baseImponible;
	
	
	
}
