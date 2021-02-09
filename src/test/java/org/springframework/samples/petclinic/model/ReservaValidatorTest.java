package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.service.UtilService;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ReservaValidatorTest {
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	private UtilService utilService;
	private Reserva reserva;
	
	@BeforeEach
	void setup() { //Las reglas semánticas las abordamos con excepciones....
		this.utilService= new UtilService();
		
		Reserva reserva= new Reserva();
		reserva.setDescripcionEquipaje("Maleta grande");
		
		reserva.setFechaSalida(new Date());
		reserva.setFechaLlegada(utilService.addFecha(new Date(), Calendar.HOUR, 1));
		reserva.setHoraSalida(new Date());
		reserva.setHoraLlegada(utilService.addFecha(new Date(), Calendar.HOUR, 1)); 
		reserva.setHorasEspera(0.0);
		reserva.setId(1);
		reserva.setNumKmTotales(123.0);
		reserva.setPlazas_Ocupadas(3);
		this.reserva=reserva;
	}
	
	@Test
	void shouldNotValidateWhenPlazasOcupadasMayorQue6() {
		
	
		this.reserva.setPlazas_Ocupadas(7);
		
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Reserva>> constraintViolations = validator.validate(reserva);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Reserva> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("plazas_Ocupadas");
		assertThat(violation.getMessage()).isEqualTo("tiene que ser menor o igual que 6");
		
		
		
	}
	
	@Test
	void shouldNotValidateWhenDescripcionEquipajeMas280Caracteres() {
		
	
				this.reserva.setDescripcionEquipaje("Esta es una cadena con más de 280 caracteres.........."
						+ "awdwadawdwaddddddddddddddddddddddddddddddddddddddddddddd"
						+ "dddddddddddddddddddddddddddddddddddddddddddddddddd"
						+ "dddddddddddddddddddddddddddddddddddddd"
						+ "dddddddddddddddddddddddddddddaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
						+ "ddddddddddddddddddddddddaaaaaaaaa");
				Validator validator = createValidator();
				Set<ConstraintViolation<Reserva>> constraintViolations = validator.validate(reserva);
				assertThat(constraintViolations.size()).isEqualTo(1);
				ConstraintViolation<Reserva> violation = constraintViolations.iterator().next();
				assertThat(violation.getPropertyPath().toString()).isEqualTo("descripcionEquipaje");
				assertThat(violation.getMessage()).isEqualTo("el tamaño tiene que estar entre 0 y 280");
		
	}
	
	@Test
	void shouldNotValidateWhenHorasEsperaNegativas() {
		
				reserva.setHorasEspera(-1.0);
				
				Validator validator = createValidator();
				Set<ConstraintViolation<Reserva>> constraintViolations = validator.validate(reserva);
				assertThat(constraintViolations.size()).isEqualTo(1);
				ConstraintViolation<Reserva> violation = constraintViolations.iterator().next();
				assertThat(violation.getPropertyPath().toString()).isEqualTo("horasEspera");
				assertThat(violation.getMessage()).isEqualTo("tiene que ser mayor o igual que 0");
		
	}
	
	
	@Test
	void shouldNotValidateWhenPlazasOcupadasMenorQue1() {
		
				reserva.setPlazas_Ocupadas(-1);
		
				Validator validator = createValidator();
				Set<ConstraintViolation<Reserva>> constraintViolations = validator.validate(reserva);
				assertThat(constraintViolations.size()).isEqualTo(1);
				ConstraintViolation<Reserva> violation = constraintViolations.iterator().next();
				assertThat(violation.getPropertyPath().toString()).isEqualTo("plazas_Ocupadas");
				assertThat(violation.getMessage()).isEqualTo("tiene que ser mayor o igual que 1");
		
	}
	
	@Test
	void shouldNotValidateWhenPrecioTotalNegativo() {
		
				reserva.setPlazas_Ocupadas(-1);
		
				Validator validator = createValidator();
				Set<ConstraintViolation<Reserva>> constraintViolations = validator.validate(reserva);
				assertThat(constraintViolations.size()).isEqualTo(1);
				ConstraintViolation<Reserva> violation = constraintViolations.iterator().next();
				assertThat(violation.getPropertyPath().toString()).isEqualTo("plazas_Ocupadas");
				assertThat(violation.getMessage()).isEqualTo("tiene que ser mayor o igual que 1");
		
	}
	
	@Test
	void shouldNotValidateWhenKilometrosTotalesNegativos() {
		
				reserva.setNumKmTotales(-1.0);
		
				Validator validator = createValidator();
				Set<ConstraintViolation<Reserva>> constraintViolations = validator.validate(reserva);
				assertThat(constraintViolations.size()).isEqualTo(1);
				ConstraintViolation<Reserva> violation = constraintViolations.iterator().next();
				assertThat(violation.getPropertyPath().toString()).isEqualTo("numKmTotales");
				assertThat(violation.getMessage()).isEqualTo("tiene que ser mayor o igual que 0");
		
	}
	
	@Test
	void shouldNotValidateWhenFechaSalidaNula() {
		
			reserva.setFechaSalida(null);
			
				Validator validator = createValidator();
				Set<ConstraintViolation<Reserva>> constraintViolations = validator.validate(reserva);
				assertThat(constraintViolations.size()).isEqualTo(1);
				ConstraintViolation<Reserva> violation = constraintViolations.iterator().next();
				assertThat(violation.getPropertyPath().toString()).isEqualTo("fechaSalida");
				assertThat(violation.getMessage()).isEqualTo("no puede ser null");
		
	}
	
	@Test
	void shouldNotValidateWhenHoraSalidaNula() {
		
			reserva.setHoraSalida(null);
			
				Validator validator = createValidator();
				Set<ConstraintViolation<Reserva>> constraintViolations = validator.validate(reserva);
				assertThat(constraintViolations.size()).isEqualTo(1);
				ConstraintViolation<Reserva> violation = constraintViolations.iterator().next();
				assertThat(violation.getPropertyPath().toString()).isEqualTo("horaSalida");
				assertThat(violation.getMessage()).isEqualTo("no puede ser null");
		
	}
	

	
	
	

	@Test
	void shouldNotValidateWhenPlazasOcupadasNulas() {
		
			reserva.setPlazas_Ocupadas(null);;
				Validator validator = createValidator();
				Set<ConstraintViolation<Reserva>> constraintViolations = validator.validate(reserva);
				assertThat(constraintViolations.size()).isEqualTo(1);
				ConstraintViolation<Reserva> violation = constraintViolations.iterator().next();
				assertThat(violation.getPropertyPath().toString()).isEqualTo("plazas_Ocupadas");
				assertThat(violation.getMessage()).isEqualTo("no puede ser null");
		
	}
	
	@Test
	void shouldNotValidateWhenHorasEsperaMasDe2Decimales() {
		
			reserva.setHorasEspera(2.2222);
				Validator validator = createValidator();
				Set<ConstraintViolation<Reserva>> constraintViolations = validator.validate(reserva);
				assertThat(constraintViolations.size()).isEqualTo(1);
				ConstraintViolation<Reserva> violation = constraintViolations.iterator().next();
				assertThat(violation.getPropertyPath().toString()).isEqualTo("horasEspera");
				assertThat(violation.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <3 dígitos>.<2 dígitos)");
		
	}
	
	@Test
	void shouldNotValidateWhenPrecioTotalConMasDe2Decimales() {
		
			reserva.setPrecioTotal(157.4587);
				Validator validator = createValidator();
				Set<ConstraintViolation<Reserva>> constraintViolations = validator.validate(reserva);
				assertThat(constraintViolations.size()).isEqualTo(1);
				ConstraintViolation<Reserva> violation = constraintViolations.iterator().next();
				assertThat(violation.getPropertyPath().toString()).isEqualTo("precioTotal");
				assertThat(violation.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <5 dígitos>.<2 dígitos)");
		
	}
	
	@Test
	void shouldNotValidateWhennumKmTotalesConMasDe2Decimales() {
		
			reserva.setNumKmTotales(157.4587);
				Validator validator = createValidator();
				Set<ConstraintViolation<Reserva>> constraintViolations = validator.validate(reserva);
				assertThat(constraintViolations.size()).isEqualTo(1);
				ConstraintViolation<Reserva> violation = constraintViolations.iterator().next();
				assertThat(violation.getPropertyPath().toString()).isEqualTo("numKmTotales");
				assertThat(violation.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <6 dígitos>.<2 dígitos)");
		
	}
	
	
	
	

}
