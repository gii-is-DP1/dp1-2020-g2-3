package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Michael Isvy Simple test to make sure that Bean Validation is working (useful
 * when upgrading to a new version of Hibernate Validator/ Bean Validation)
 */
class ContratoValidatorTest {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenSalarioMensualEmpty() {

		Contrato contrato = new Contrato();
		Date fechaInicio = new Date();
		Date fechaFin = new Date();
		contrato.setSalarioMensual(null);
		contrato.setFechaInicio(fechaInicio);
		contrato.setFechaFin(fechaFin);
		

		Validator validator = createValidator();
		Set<ConstraintViolation<Contrato>> constraintViolations = validator.validate(contrato);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Contrato> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("salarioMensual");
		assertThat(violation.getMessage()).isEqualTo("no puede ser null");
	}
	
	@Test
	void shouldNotValidateWhenfechaInicioEmpty() {

		Contrato contrato = new Contrato();
		Date fechaFin = new Date();
		contrato.setSalarioMensual(1523.05);
		contrato.setFechaInicio(null);
		contrato.setFechaFin(fechaFin);
		

		Validator validator = createValidator();
		Set<ConstraintViolation<Contrato>> constraintViolations = validator.validate(contrato);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Contrato> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("fechaInicio");
		assertThat(violation.getMessage()).isEqualTo("no puede ser null");
	}
	
	@Test
	void shouldNotValidateWhenfechaFinEmpty() {

		Contrato contrato = new Contrato();
		Date fechaInicio = new Date();
		contrato.setSalarioMensual(1523.05);
		contrato.setFechaInicio(fechaInicio);
		contrato.setFechaFin(null);
		

		Validator validator = createValidator();
		Set<ConstraintViolation<Contrato>> constraintViolations = validator.validate(contrato);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Contrato> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("fechaFin");
		assertThat(violation.getMessage()).isEqualTo("no puede ser null");
	}
	


	@Test
	void shouldNotValidateWhenFechaInicioPatternIsWrong() {

		Contrato contrato = new Contrato();
		Date fechaInicio = new Date();
		Date fechaFin = new Date();
		contrato.setSalarioMensual(null);
		contrato.setFechaInicio(fechaInicio);
		contrato.setFechaFin(fechaFin);

		Validator validator = createValidator();
		Set<ConstraintViolation<Contrato>> constraintViolations = validator.validate(contrato);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Contrato> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("fechaInicio");
		assertThat(violation.getMessage()).isEqualTo("tiene que corresponder a la expresión regular \"[0-9]{8}[A-Za-z]{1}\"");
	}

}
