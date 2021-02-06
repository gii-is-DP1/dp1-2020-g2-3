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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
@Data
@Entity
@Table(name = "trabajador")
public class Trabajador extends Person {
	
	
	@Column(name = "email")
	@Email
	private  String email;
	
	@Column(name = "telefono")
	@Digits(fraction = 0, integer = 10)
	@NotEmpty
	private  String telefono;
	
	
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "tipo_trabajador_id",referencedColumnName="id")
	private  TipoTrabajador tipoTrabajador;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "contrato", referencedColumnName = "id")
	private Contrato contrato;

	@Override
	public String toString() {
		return "Trabajador [email=" + email + ", telefono=" + telefono + ", user=" + user + ", tipoTrabajador="
				+ tipoTrabajador + ", contrato=" + contrato + "]";
	}
	



	
	
}
