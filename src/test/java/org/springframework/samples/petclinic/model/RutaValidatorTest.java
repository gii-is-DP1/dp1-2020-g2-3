package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class RutaValidatorTest {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'origenCliente' es vacío.")
	void shouldNotValidateWhenOrigenClienteEmpty() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("");
		r.setDestinoCliente("Barcelona");
		r.setNumKmTotales(150.0);
		r.setHorasEstimadasCliente(5.0);
		r.setHorasEstimadasTaxista(6.0);
		
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("origenCliente");
		assertThat(con.getMessage()).isEqualTo("no puede estar vacío");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'destinoCliente' es vacío.")
	void shouldNotValidateWhenDestinoClienteEmpty() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("");
		r.setNumKmTotales(150.0);
		r.setHorasEstimadasCliente(5.0);
		r.setHorasEstimadasTaxista(6.0);
		
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("destinoCliente");
		assertThat(con.getMessage()).isEqualTo("no puede estar vacío");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'numKmTotales' a null.")
	void shouldNotValidateWhenNumKmTotalesNull() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("Madrid");
		r.setNumKmTotales(null);
		r.setHorasEstimadasCliente(5.0);
		r.setHorasEstimadasTaxista(6.0);
				
				
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
				
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("numKmTotales");
		assertThat(con.getMessage()).isEqualTo("no puede ser null");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'horasEstimadasCliente' a null.")
	void shouldNotValidateWhenHorasEstimadasClienteNull() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("Madrid");
		r.setNumKmTotales(150.0);
		r.setHorasEstimadasCliente(null);
		r.setHorasEstimadasTaxista(6.0);
				
				
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
				
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("horasEstimadasCliente");
		assertThat(con.getMessage()).isEqualTo("no puede ser null");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'horasEstimadasTaxista' a null.")
	void shouldNotValidateWhenHorasEstimadasTaxistaNull() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("Madrid");
		r.setNumKmTotales(150.0);
		r.setHorasEstimadasCliente(5.0);
		r.setHorasEstimadasTaxista(null);
				
				
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
				
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("horasEstimadasTaxista");
		assertThat(con.getMessage()).isEqualTo("no puede ser null");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'numKmTotales' menor que 0.")
	void shouldNotValidateWhenNumKmTotalesLessThan0() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("Madrid");
		r.setNumKmTotales(-50.0);
		r.setHorasEstimadasCliente(5.0);
		r.setHorasEstimadasTaxista(6.0);
				
				
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
				
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("numKmTotales");
		assertThat(con.getMessage()).isEqualTo("tiene que ser mayor o igual que 0");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'numKmTotales' con más de 6 digitos en su parte entera.")
	void shouldNotValidateWhenNumKmTotalesHasMoreThan6DigitsInHisIntegerPart() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("Madrid");
		r.setNumKmTotales(10000000.0);
		r.setHorasEstimadasCliente(5.0);
		r.setHorasEstimadasTaxista(6.0);
				
				
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
				
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("numKmTotales");
		assertThat(con.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <6 dígitos>.<2 dígitos)");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'numKmTotales' con más de 2 digitos en su parte decimal.")
	void shouldNotValidateWhenNumKmTotalesHasMoreThan2DigitsInHisFractionPart() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("Madrid");
		r.setNumKmTotales(1000.456);
		r.setHorasEstimadasCliente(5.0);
		r.setHorasEstimadasTaxista(6.0);
				
				
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
				
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("numKmTotales");
		assertThat(con.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <6 dígitos>.<2 dígitos)");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'horasEstimadasCliente' menor que 0.")
	void shouldNotValidateWhenHorasEstimadasClienteLessThan0() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("Madrid");
		r.setNumKmTotales(50.0);
		r.setHorasEstimadasCliente(-5.0);
		r.setHorasEstimadasTaxista(6.0);
				
				
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
				
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("horasEstimadasCliente");
		assertThat(con.getMessage()).isEqualTo("tiene que ser mayor o igual que 0");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'horasEstimadasCliente' con más de 3 digitos en su parte entera.")
	void shouldNotValidateWhenHorasEstimadasClienteHasMoreThan3DigitsInHisIntegerPart() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("Madrid");
		r.setNumKmTotales(10000.0);
		r.setHorasEstimadasCliente(55555.0);
		r.setHorasEstimadasTaxista(6.0);
				
				
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
				
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("horasEstimadasCliente");
		assertThat(con.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <3 dígitos>.<2 dígitos)");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'horasEstimadasCliente' con más de 2 digitos en su parte decimal.")
	void shouldNotValidateWhenHorasEstimadasClienteHasMoreThan2DigitsInHisFractionPart() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("Madrid");
		r.setNumKmTotales(1000.0);
		r.setHorasEstimadasCliente(5.444);
		r.setHorasEstimadasTaxista(6.0);
				
				
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
				
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("horasEstimadasCliente");
		assertThat(con.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <3 dígitos>.<2 dígitos)");
	}
	
	//
	@Test
	@DisplayName("No debe validarse con el atributo 'horasEstimadasTaxista' menor que 0.")
	void shouldNotValidateWhenHorasEstimadasTaxistaLessThan0() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("Madrid");
		r.setNumKmTotales(50.0);
		r.setHorasEstimadasCliente(5.0);
		r.setHorasEstimadasTaxista(-6.0);
				
				
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
				
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("horasEstimadasTaxista");
		assertThat(con.getMessage()).isEqualTo("tiene que ser mayor o igual que 0");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'horasEstimadasTaxista' con más de 3 digitos en su parte entera.")
	void shouldNotValidateWhenHorasEstimadasTaxistaHasMoreThan3DigitsInHisIntegerPart() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("Madrid");
		r.setNumKmTotales(10000.0);
		r.setHorasEstimadasCliente(5.0);
		r.setHorasEstimadasTaxista(6666.0);
				
				
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
				
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("horasEstimadasTaxista");
		assertThat(con.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <3 dígitos>.<2 dígitos)");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo horasEstimadasTaxista' con más de 2 digitos en su parte decimal.")
	void shouldNotValidateWhenHorasEstimadasTaxistaHasMoreThan2DigitsInHisFractionPart() {
		
		//ARRANGE
		Ruta r = new Ruta();
		r.setOrigenCliente("Barcelona");
		r.setDestinoCliente("Madrid");
		r.setNumKmTotales(1000.0);
		r.setHorasEstimadasCliente(5.0);
		r.setHorasEstimadasTaxista(6.111);
				
				
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Ruta>> c = v.validate(r);
				
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Ruta> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("horasEstimadasTaxista");
		assertThat(con.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <3 dígitos>.<2 dígitos)");
	}
	
	
}
