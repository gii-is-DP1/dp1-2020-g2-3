package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ClienteValidatorTest {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldNotValidateWhenTelefonoNotCorrectPattern() {
		
		Cliente cliente = new Cliente();
		cliente.setNombre("Roberto");
		cliente.setApellidos("Garcia");
		cliente.setDni("23002323H");
		cliente.setEmail("roberto@gmail.com");
		cliente.setTelefono("1212123131313");


		Validator validator = createValidator();
		Set<ConstraintViolation<Cliente>> constraintViolations = validator.validate(cliente);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Cliente> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("telefono");
		assertThat(violation.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <10 dígitos>.<0 dígitos)");
	}
	
	@Test
	void shouldNotValidateWhenEmailNotCorrectPattern() {
		
		Cliente cliente = new Cliente();
		cliente.setNombre("Roberto");
		cliente.setApellidos("Garcia");
		cliente.setDni("23002323H");
		cliente.setEmail("roberto/gmail.com");
		cliente.setTelefono("678726127");


		Validator validator = createValidator();
		Set<ConstraintViolation<Cliente>> constraintViolations = validator.validate(cliente);

		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Cliente> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
		assertThat(violation.getMessage()).isEqualTo("no es una dirección de correo bien formada");
	}
}
