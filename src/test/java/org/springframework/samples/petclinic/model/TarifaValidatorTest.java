package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class TarifaValidatorTest {
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldNotValidateWhenPrecioPorKmEmpty() {
	Tarifa nuevaTarifa= new Tarifa();
	nuevaTarifa.setPrecioPorKm(null);
	nuevaTarifa.setPrecioEsperaPorHora(2.0);
	nuevaTarifa.setPorcentajeIvaRepercutido(10);
	nuevaTarifa.setOriginal(true);
	nuevaTarifa.setActivado(true);
	
	Validator validator = createValidator();
	Set<ConstraintViolation<Tarifa>> constraintViolations = validator.validate(nuevaTarifa);

	assertThat(constraintViolations.size()).isEqualTo(1);
	ConstraintViolation<Tarifa> violation = constraintViolations.iterator().next();
	assertThat(violation.getPropertyPath().toString()).isEqualTo("precioPorKm");
	assertThat(violation.getMessage()).isEqualTo("no puede ser null");
	
	}
	
	@Test
	void shouldNotValidateWhenPorcentajeIvaRepercutidoEmpty() {
	Tarifa nuevaTarifa= new Tarifa();
	nuevaTarifa.setPrecioPorKm(4.0);
	nuevaTarifa.setPrecioEsperaPorHora(2.0);
	nuevaTarifa.setPorcentajeIvaRepercutido(null);
	nuevaTarifa.setOriginal(true);
	nuevaTarifa.setActivado(true);
	
	Validator validator = createValidator();
	Set<ConstraintViolation<Tarifa>> constraintViolations = validator.validate(nuevaTarifa);

	assertThat(constraintViolations.size()).isEqualTo(1);
	ConstraintViolation<Tarifa> violation = constraintViolations.iterator().next();
	assertThat(violation.getPropertyPath().toString()).isEqualTo("porcentajeIvaRepercutido");
	assertThat(violation.getMessage()).isEqualTo("no puede ser null");
	
	}
	
	@Test
	void shouldNotValidateWhenPrecioEsperaPorHoraEmpty() {
	Tarifa nuevaTarifa= new Tarifa();
	nuevaTarifa.setPrecioPorKm(4.0);
	nuevaTarifa.setPrecioEsperaPorHora(null);
	nuevaTarifa.setPorcentajeIvaRepercutido(10);
	nuevaTarifa.setOriginal(true);
	nuevaTarifa.setActivado(true);
	
	Validator validator = createValidator();
	Set<ConstraintViolation<Tarifa>> constraintViolations = validator.validate(nuevaTarifa);

	assertThat(constraintViolations.size()).isEqualTo(1);
	ConstraintViolation<Tarifa> violation = constraintViolations.iterator().next();
	assertThat(violation.getPropertyPath().toString()).isEqualTo("precioEsperaPorHora");
	assertThat(violation.getMessage()).isEqualTo("no puede ser null");
	
	}

}
