package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

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
class PersonValidatorTest {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenFirstNameEmpty() {

		Person person = new Person();
		person.setNombre("");
		person.setApellidos("smith");
		person.setDni("23887112Q");

		Validator validator = createValidator();
		Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Person> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("nombre");
		assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");
	}
	
	@Test
	void shouldNotValidateWhenLastNameEmpty() {

		Person person = new Person();
		person.setNombre("Paco");
		person.setApellidos("");
		person.setDni("23887112Q");

		Validator validator = createValidator();
		Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Person> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("apellidos");
		assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");
	}
	
	@Test
	void shouldNotValidateWhenDniEmpty() {

		Person person = new Person();
		person.setNombre("Paco");
		person.setApellidos("Smith");

		Validator validator = createValidator();
		Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Person> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("dni");
		assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");
	}
	
	@Test
	void shouldNotValidateWhenDniPatternIsWrong() {

		Person person = new Person();
		person.setNombre("Paco");
		person.setApellidos("Smith");
		person.setDni("3a4l1");

		Validator validator = createValidator();
		Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Person> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("dni");
		assertThat(violation.getMessage()).isEqualTo("tiene que corresponder a la expresión regular \"[0-9]{8}[A-Za-z]{1}\"");
	}

}
