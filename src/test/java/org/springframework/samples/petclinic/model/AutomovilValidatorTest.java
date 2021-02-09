package org.springframework.samples.petclinic.model;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AutomovilValidatorTest {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'marca' a null.")
	void shouldNotValidateWhenMarcaEmpty() {
		
		//ARRANGE
		Automovil a = new Automovil();
		a.setId(1);
		a.setKmRecorridos(25000.00);
		a.setMarca(null);
		a.setModelo("350Z");
		a.setNumPlazas(4);
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Automovil>> c = v.validate(a);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Automovil> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("marca");
		assertThat(con.getMessage()).isEqualTo("no puede estar vacío");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'modelo' a null.")
	void shouldNotValidateWhenModeloEmpty() {
		
		//ARRANGE
		Automovil a = new Automovil();
		a.setId(1);
		a.setKmRecorridos(25000.00);
		a.setMarca("Nissan");
		a.setModelo(null);
		a.setNumPlazas(4);
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Automovil>> c = v.validate(a);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Automovil> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("modelo");
		assertThat(con.getMessage()).isEqualTo("no puede estar vacío");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'numPlazas' a null.")
	void shouldNotValidateWhenNumPlazasEmpty() {
		
		//ARRANGE
		Automovil a = new Automovil();
		a.setId(1);
		a.setKmRecorridos(25000.00);
		a.setMarca("Nissan");
		a.setModelo("350Z");
		a.setNumPlazas(null);
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Automovil>> c = v.validate(a);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Automovil> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("numPlazas");
		assertThat(con.getMessage()).isEqualTo("no puede ser null");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'kmRecorridos' a null.")
	void shouldNotValidateWhenKmRecorridosEmpty() {
		
		//ARRANGE
		Automovil a = new Automovil();
		a.setId(1);
		a.setKmRecorridos(null);
		a.setMarca("Nissan");
		a.setModelo("350Z");
		a.setNumPlazas(4);
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Automovil>> c = v.validate(a);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Automovil> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("kmRecorridos");
		assertThat(con.getMessage()).isEqualTo("no puede ser null");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'marca' con más de 25 caracteres.")
	void shouldNotValidateWhenMarcaIsBiggerThan25Characters() {
		
		//ARRANGE
		Automovil a = new Automovil();
		a.setId(1);
		a.setKmRecorridos(25.00);
		a.setMarca("NissanNissanNissanNissanNissanNissanNissanNissanNissanNissanNissanNissanNissanNissan");
		a.setModelo("350Z");
		a.setNumPlazas(4);
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Automovil>> c = v.validate(a);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Automovil> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("marca");
		assertThat(con.getMessage()).isEqualTo("el tamaño tiene que estar entre 1 y 25");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'modelo' con más de 40 caracteres.")
	void shouldNotValidateWhenModeloIsBiggerThan40Characters() {
		
		//ARRANGE
		Automovil a = new Automovil();
		a.setId(1);
		a.setKmRecorridos(25.00);
		a.setMarca("Nissan");
		a.setModelo("350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z350Z");
		a.setNumPlazas(4);
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Automovil>> c = v.validate(a);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Automovil> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("modelo");
		assertThat(con.getMessage()).isEqualTo("el tamaño tiene que estar entre 1 y 40");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'numPlazas' mayor que 7.")
	void shouldNotValidateWhenNumPlazasisBiggerThan7() {
		
		//ARRANGE
		Automovil a = new Automovil();
		a.setId(1);
		a.setKmRecorridos(25.00);
		a.setMarca("Nissan");
		a.setModelo("350Z");
		a.setNumPlazas(8);
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Automovil>> c = v.validate(a);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Automovil> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("numPlazas");
		assertThat(con.getMessage()).isEqualTo("tiene que ser menor o igual que 7");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'numPlazas' menor que 1.")
	void shouldNotValidateWhenNumPlazasIsLessThan1() {
		
		//ARRANGE
		Automovil a = new Automovil();
		a.setId(1);
		a.setKmRecorridos(25.00);
		a.setMarca("Nissan");
		a.setModelo("350Z");
		a.setNumPlazas(0);
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Automovil>> c = v.validate(a);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Automovil> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("numPlazas");
		assertThat(con.getMessage()).isEqualTo("tiene que ser mayor o igual que 1");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'kmRecorridos' menor que 0.")
	void shouldNotValidateWhenKmRecorridosIsLessThan0() {
		
		//ARRANGE
		Automovil a = new Automovil();
		a.setId(1);
		a.setKmRecorridos(-25.00);
		a.setMarca("Nissan");
		a.setModelo("350Z");
		a.setNumPlazas(4);
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Automovil>> c = v.validate(a);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Automovil> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("kmRecorridos");
		assertThat(con.getMessage()).isEqualTo("tiene que ser mayor o igual que 0");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'KmRecorridos' con más de 6 números en su parte entera.")
	void shouldNotValidateWhenKmRecorridosHasMoreThan6NumbersInTheIntegerPart() {
		
		//ARRANGE
		Automovil a = new Automovil();
		a.setId(1);
		a.setKmRecorridos(25000000.00);
		a.setMarca("Nissan");
		a.setModelo("350Z");
		a.setNumPlazas(4);
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Automovil>> c = v.validate(a);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Automovil> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("kmRecorridos");
		assertThat(con.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <6 dígitos>.<2 dígitos)");
	}
	
	@Test
	@DisplayName("No debe validarse con el atributo 'KmRecorridos' con más de 2 números en su parte decimal.")
	void shouldNotValidateWhenKmRecorridosHasMoreThan2NumbersInTheFractionPart() {
		
		//ARRANGE
		Automovil a = new Automovil();
		a.setId(1);
		a.setKmRecorridos(2500.541);
		a.setMarca("Nissan");
		a.setModelo("350Z");
		a.setNumPlazas(4);
		
		//ACT
		Validator v = createValidator();
		Set<ConstraintViolation<Automovil>> c = v.validate(a);
		
		//ASSERT
		assertThat(c.size()).isEqualTo(1);
		ConstraintViolation<Automovil> con = c.iterator().next();
		assertThat(con.getPropertyPath().toString()).isEqualTo("kmRecorridos");
		assertThat(con.getMessage()).isEqualTo("valor numérico fuera de los límites (se esperaba <6 dígitos>.<2 dígitos)");
	}	
}
