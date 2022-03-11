package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

public class OwnerTests {

	private Owner owner = new Owner();

	@BeforeEach
	void setup() {
		owner.setPetsInternal(null);
		owner.setAddress("101 Carlton");
		owner.setCity("Montreal");
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setId(123);
		owner.setTelephone("514-123-4567");
	}

	@Test
	void testSerialization() {
		Owner other = (Owner) SerializationUtils.deserialize(SerializationUtils.serialize(owner));
		assertThat(other.getFirstName()).isEqualTo(owner.getFirstName());
		assertThat(other.getLastName()).isEqualTo(owner.getLastName());
		assertThat(other.getId()).isEqualTo(owner.getId());
		assertThat(other.getAddress()).isEqualTo(owner.getAddress());
		assertThat(other.getCity()).isEqualTo(owner.getCity());
		assertThat(other.getTelephone()).isEqualTo(owner.getTelephone());
	}

	@Test
	void testAddPetToEmptySet() {
		Pet pet = new Pet();
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		owner.addPet(pet);
		assertTrue(owner.getPetsInternal().contains(pet));
	}

	@Test
	void testAddPetWithIdToEmptySet() {
		Pet pet = new Pet();
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		pet.setId(123);
		owner.addPet(pet);
		assertFalse(owner.getPetsInternal().contains(pet));
	}

	@Test
	void testAddToExistingSet() {
		Pet pet1 = new Pet();
		pet1.setName("Charlie");
		PetType petType1 = new PetType();
		petType1.setName("Cat");
		pet1.setType(petType1);
		LocalDate birthDate1 = LocalDate.of(2000, 1, 1);
		pet1.setBirthDate(birthDate1);
		owner.addPet(pet1);
		Pet pet2 = new Pet();
		pet2.setName("Ben");
		PetType petType2 = new PetType();
		petType2.setName("Dog");
		pet2.setType(petType2);
		LocalDate birthDate2 = LocalDate.of(2008, 8, 8);
		pet2.setBirthDate(birthDate2);
		owner.addPet(pet2);
		assertTrue(owner.getPetsInternal().contains(pet1));
		assertTrue(owner.getPetsInternal().contains(pet2));
	}

	@Test
	void testAddTwoPetsWithSameName() {
		Pet pet1 = new Pet();
		pet1.setName("Charlie");
		PetType petType1 = new PetType();
		petType1.setName("Cat");
		pet1.setType(petType1);
		LocalDate birthDate1 = LocalDate.of(2000, 1, 1);
		pet1.setBirthDate(birthDate1);
		owner.addPet(pet1);
		Pet pet2 = new Pet();
		pet2.setName("Charlie");
		PetType petType2 = new PetType();
		petType2.setName("Dog");
		pet2.setType(petType2);
		LocalDate birthDate2 = LocalDate.of(2008, 8, 8);
		pet2.setBirthDate(birthDate2);
		owner.addPet(pet2);
		assertTrue(owner.getPetsInternal().contains(pet1));
		assertTrue(owner.getPetsInternal().contains(pet2));
	}

	@Test
	void testGetPetFromEmptySet() {
		Pet pet = new Pet();
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		pet.setId(123);
		assertNull(owner.getPet("Charlie"));
	}

	@Test
	void testGetPetWithDontIgnoreNewAndNewPet() {
		Pet pet = new Pet();
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		owner.addPet(pet);
		assertNotNull(owner.getPet("Charlie"));
	}

	@Test
	void testGetPetWithIgnoreNewAndNewPet() {
		Pet pet = new Pet();
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		owner.addPet(pet);
		assertNull(owner.getPet("Charlie", true));
	}

	@Test
	void testGetPetWithDontIgnoreNewAndOldPet() {
		Pet pet = new Pet();
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		owner.addPet(pet);
		owner.setId(123);
		assertNotNull(owner.getPet("Charlie"));
	}

	@Test
	void testGetPetWithIgnoreNewAndOldPet() {
		Pet pet = new Pet();
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		owner.addPet(pet);
		pet.setId(123);
		assertNotNull(owner.getPet("Charlie", true));
	}

	@Test
	void testGetPetWithNonExistingPet() {
		Pet pet = new Pet();
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		owner.addPet(pet);
		assertNull(owner.getPet("Ben"));
	}

	@Test
	void testGetPetWhenTwoPetsHaveSameName() { // GetPet has issues when two pets have same name
		Pet pet1 = new Pet();
		pet1.setName("Charlie");
		PetType petType1 = new PetType();
		petType1.setName("Cat");
		pet1.setType(petType1);
		LocalDate birthDate1 = LocalDate.of(2000, 1, 1);
		pet1.setBirthDate(birthDate1);
		owner.addPet(pet1);
		Pet pet2 = new Pet();
		pet2.setName("Charlie");
		PetType petType2 = new PetType();
		petType2.setName("Dog");
		pet2.setType(petType2);
		LocalDate birthDate2 = LocalDate.of(2008, 8, 8);
		pet2.setBirthDate(birthDate2);
		owner.addPet(pet2);
		assertEquals(2, owner.getPetsInternal().size());
		assertEquals("Charlie", owner.getPet("Charlie").getName());
	}

	@Test
	void testGetPetsWhenOneExists() {
		Set<Pet> pets = new HashSet<Pet>();
		Pet pet1 = new Pet();
		pet1.setName("Charlie");
		PetType petType1 = new PetType();
		petType1.setName("Cat");
		pet1.setType(petType1);
		LocalDate birthDate1 = LocalDate.of(2000, 1, 1);
		pet1.setBirthDate(birthDate1);
		pets.add(pet1);
		Pet pet2 = new Pet();
		pet2.setName("Ben");
		PetType petType2 = new PetType();
		petType2.setName("Dog");
		pet2.setType(petType2);
		LocalDate birthDate2 = LocalDate.of(2008, 8, 8);
		pet2.setBirthDate(birthDate2);
		pets.add(pet2);
		owner.setPetsInternal(pets);
		List<Pet> list = owner.getPets();
		assertTrue(list.contains(pet1));
		assertTrue(list.contains(pet2));
	}

	@Test
	void testGetPetsWhenNoneExist() {
		List<Pet> list = owner.getPets();
		assertTrue(list.isEmpty());
	}

	@Test
	void testToString() {
		Boolean result = owner.toString().contains("id = 123");
		assertTrue(result);
	}

	@Test
	void testAddPetWherePetBelongsToTwoOwners() { // Weird issue where two owners can have the same pet, but a pet can
													// only have 1 owner
		Owner owner2 = new Owner();
		owner2.setPetsInternal(null);
		owner2.setAddress("107 Carlton");
		owner2.setCity("Montreal");
		owner2.setFirstName("Jane");
		owner2.setLastName("Doe");
		owner2.setId(321);
		owner2.setTelephone("514-111-1111");
		Pet pet = new Pet();
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = LocalDate.of(2000, 1, 1);
		pet.setBirthDate(birthDate);
		owner.addPet(pet);
		assertEquals(owner.getFirstName(), pet.getOwner().getFirstName());
		owner2.addPet(pet);
		assertEquals(owner2.getFirstName(), pet.getOwner().getFirstName());
		assertTrue(owner.getPetsInternal().contains(pet));
		assertTrue(owner2.getPetsInternal().contains(pet));
	}

}
