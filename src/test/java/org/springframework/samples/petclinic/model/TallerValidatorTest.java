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
class TallerValidatorTest {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenUbicacionEmpty() {

		Taller taller = new Taller();
		taller.setUbicacion("");
		taller.setTelefono(658987412);
	

		Validator validator = createValidator();
		Set<ConstraintViolation<Taller>> constraintViolations = validator.validate(taller);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Taller> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("ubicacion");
		assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");
	}
	
	@Test
	void shouldNotValidateWhenTelefonoEmpty() {

		Taller taller = new Taller();
		taller.setUbicacion("Sevilla");
		taller.setTelefono(null);
	
		Validator validator = createValidator();
		Set<ConstraintViolation<Taller>> constraintViolations = validator.validate(taller);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Taller> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("telefono");
		assertThat(violation.getMessage()).isEqualTo("no puede ser null");
	}
	
}
