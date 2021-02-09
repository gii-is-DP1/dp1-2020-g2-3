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

public class TrayectoValidatorTest {
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	private UtilService utilService;
	private Trayecto trayecto;
	
	@BeforeEach
	void setup() { //Las reglas semánticas las abordamos con excepciones....
		Trayecto trayecto= new Trayecto();
		trayecto.setOrigen("Jerez de los Caballeros");
		trayecto.setDestino("Zahinos");
		trayecto.setId(1);
		trayecto.setNumKmTotales(169.12);
		trayecto.setHorasEstimadas(1.5);
		this.trayecto=trayecto;
	}
	
	@Test
	void shouldNotValidateWhenOrigenEmpty() {
		
	
		this.trayecto.setOrigen("");
		
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trayecto>> constraintViolations = validator.validate(trayecto);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trayecto> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("origen");
		assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");
		
		
		
	}
	
	@Test
	void shouldNotValidateWhenDestinoEmpty() {
		
	
		this.trayecto.setDestino("");
		
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trayecto>> constraintViolations = validator.validate(trayecto);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trayecto> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("destino");
		assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");
		
		
	}
	

	@Test
	void shouldNotValidateWhenNumKmTotalesNegativo() {
		
	
		this.trayecto.setNumKmTotales(-1.0);
		
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trayecto>> constraintViolations = validator.validate(trayecto);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trayecto> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("numKmTotales");
		assertThat(violation.getMessage()).isEqualTo("tiene que ser mayor o igual que 0");
		
		
	}
	
	@Test
	void shouldNotValidateWhenNumKmTotalesConMasDe2Decimales() {
		
	
		this.trayecto.setNumKmTotales(123.45488);
		
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trayecto>> constraintViolations = validator.validate(trayecto);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trayecto> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("numKmTotales");
		assertThat(violation.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <6 dígitos>.<2 dígitos)");
		
		
	}
	
	@Test
	void shouldNotValidateWhenHorasEstimadasNegativas() {
		
	
		this.trayecto.setHorasEstimadas(-1.0);
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trayecto>> constraintViolations = validator.validate(trayecto);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trayecto> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("horasEstimadas");
		assertThat(violation.getMessage()).isEqualTo("tiene que ser mayor o igual que 0");
		
		
	}

	@Test
	void shouldNotValidateWhenHorasEstimadasConMasDe2Decimales() {
		
	
		this.trayecto.setHorasEstimadas(2.4572);
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trayecto>> constraintViolations = validator.validate(trayecto);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trayecto> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("horasEstimadas");
		assertThat(violation.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <3 dígitos>.<2 dígitos)");
		
		
	}
}
