package org.springframework.samples.petclinic.model;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Getter
@Setter
@Entity
@Table(name = "reserva")
public class Reserva extends BaseEntity {
	
	@ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
	private Cliente cliente;
	
	@ManyToOne
    @JoinColumn(name = "ruta_id", referencedColumnName = "id")
	private Ruta ruta;
	
	@ManyToOne
    @JoinColumn(name = "tarifa_id", referencedColumnName = "id")
	private Tarifa tarifa;
	
	@OneToOne(optional=true)
	@JoinColumn(name="automovil_id", referencedColumnName="id")
	private Automovil automovil;
	
	@OneToOne(optional=true)
	@JoinColumn(name="trabajador_id", referencedColumnName="id")
	private Trabajador trabajador;
	
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
	
	public static Reserva newReservaSinCalcular(Date fechaSalida,Date horaSalida, Integer plazasOcupadas,String descripcionEquipaje) {
		Reserva nuevaReserva= new Reserva();
		nuevaReserva.setFechaSalida(fechaSalida);
		nuevaReserva.setHoraSalida(horaSalida);
		nuevaReserva.setPlazas_Ocupadas(plazasOcupadas);
		nuevaReserva.setDescripcionEquipaje(descripcionEquipaje);
		return nuevaReserva;
	}
	
}
