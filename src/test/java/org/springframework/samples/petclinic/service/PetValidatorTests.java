package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class PetValidatorTests {

	private PetValidator petValidator = new PetValidator();
	private Pet pet = new Pet();
	private Errors errors;

	@BeforeEach
	void setup() {
		errors = new BeanPropertyBindingResult(pet, "pet");
	}

	@Test
	void testValidate() {
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		petValidator.validate(pet, errors);
		assertEquals(0, errors.getErrorCount());
	}

	@Test
	void nameRejected() {
		pet.setName("");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		petValidator.validate(pet, errors);
		assertEquals(1, errors.getFieldErrorCount("name"));
	}

	@Test
	void typeRejected() {
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType = null;
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		petValidator.validate(pet, errors);
		assertEquals(1, errors.getFieldErrorCount("type"));
	}

	@Test
	void typeNotRejectedBecausePetIsNotNew() {
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType = null;
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		pet.setId(123);
		petValidator.validate(pet, errors);
		assertEquals(0, errors.getFieldErrorCount("type"));
	}

	@Test
	void birthDateRejected() {
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = null;
		pet.setBirthDate(birthDate);
		petValidator.validate(pet, errors);
		assertEquals(1, errors.getFieldErrorCount("birthDate"));
	}

	@Test
	void supportsPetClass() {
		assertTrue(petValidator.supports(pet.getClass()));
	}

	@Test
	void doesNotSupportOtherClasses() {
		Owner owner = new Owner();
		owner.setFirstName("Adam");
		owner.setLastName("Baker");
		assertFalse(petValidator.supports(owner.getClass()));
	}

}
