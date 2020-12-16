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
import java.util.ArrayList;
import java.util.Collections;
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
@Table(name = "Ruta_Trayecto")
public class RutaTrayecto extends BaseEntity {
	
	@ManyToOne
    @JoinColumn(name = "ruta_id", referencedColumnName = "id")
	private Ruta ruta;
	
	@ManyToOne
    @JoinColumn(name = "trayecto_id", referencedColumnName = "id")
	private Trayecto trayecto;

	@Override
	public String toString() {
		return "RutaTrayecto [ruta=" + ruta + ", trayecto=" + trayecto + "]";
	}
	
	
	
}
