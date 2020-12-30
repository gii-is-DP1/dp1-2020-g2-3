package org.springframework.samples.petclinic.model;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
@Entity
@Table(name = "Reserva")
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
