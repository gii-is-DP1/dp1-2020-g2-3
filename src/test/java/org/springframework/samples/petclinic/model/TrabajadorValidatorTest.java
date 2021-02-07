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
class TrabajadorValidatorTest {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenTelefonoEmpty() {

		Trabajador trabajador = new Trabajador();

		
		trabajador.setNombre("paco");
		trabajador.setApellidos("perez");
		trabajador.setDni("12345678H");
	
	
		Validator validator = createValidator();
		Set<ConstraintViolation<Trabajador>> constraintViolations = validator.validate(trabajador);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trabajador> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("telefono");
		assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");
	}
	
	@Test
	void shouldNotValidateWhenTelefonoIsWrong() {

		Trabajador trabajador = new Trabajador();

		
		trabajador.setNombre("paco");
		trabajador.setApellidos("perez");
		trabajador.setDni("12345678H");
		trabajador.setTelefono("198720398712039");
	
	
		Validator validator = createValidator();
		Set<ConstraintViolation<Trabajador>> constraintViolations = validator.validate(trabajador);
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trabajador> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("telefono");
		assertThat(violation.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <10 dígitos>.<0 dígitos)");
	}
	
	
	
}
