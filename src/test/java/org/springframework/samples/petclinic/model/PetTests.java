package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

public class PetTests {

	private Pet pet = new Pet();

	@BeforeEach
	void setup() {
		Set<Visit> visits = new LinkedHashSet<Visit>();
		pet.setVisitsInternal(visits);
		pet.setName("Charlie");
		PetType petType = new PetType();
		petType.setName("Cat");
		pet.setType(petType);
		LocalDate birthDate = LocalDate.now();
		pet.setBirthDate(birthDate);
		pet.setId(123);
	}

	@Test
	void testSerialization() {
		Owner owner = new Owner();
		owner.setFirstName("Thomas");
		pet.setOwner(owner);
		Pet other = (Pet) SerializationUtils.deserialize(SerializationUtils.serialize(pet));
		assertThat(other.getName()).isEqualTo(pet.getName());
		assertThat(other.getType().getName()).isEqualTo(pet.getType().getName());
		assertThat(other.getBirthDate()).isEqualTo(pet.getBirthDate());
		assertThat(other.getId()).isEqualTo(pet.getId());
		assertThat(other.getOwner().getFirstName()).isEqualTo(pet.getOwner().getFirstName());
	}

	@Test
	void testAddVisitToEmptySet() {
		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setId(321);
		visit.setDescription("Checkup");
		pet.addVisit(visit);
		assertTrue(pet.getVisitsInternal().contains(visit));
	}

	@Test
	void testAddVisitToExistingSet() {
		Visit visit1 = new Visit();
		visit1.setDate(LocalDate.now());
		visit1.setId(321);
		visit1.setDescription("Checkup");
		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.of(2000, 1, 1));
		visit2.setId(555);
		visit2.setDescription("Cleaning");
		pet.addVisit(visit1);
		pet.addVisit(visit2);
		assertTrue(pet.getVisitsInternal().contains(visit1));
		assertTrue(pet.getVisitsInternal().contains(visit2));
	}

	@Test
	void testVisitsInternalOnlyContainsAddedVisits() { // this.visits == null is dead code and thus untestable
		Visit visit1 = new Visit();
		visit1.setDate(LocalDate.now());
		visit1.setId(321);
		visit1.setDescription("Checkup");
		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.of(2000, 1, 1));
		visit2.setId(555);
		visit2.setDescription("Cleaning");
		pet.addVisit(visit1);
		assertTrue(pet.getVisitsInternal().contains(visit1));
		assertFalse(pet.getVisitsInternal().contains(visit2));
	}

	@Test
	void testGetVisitsWhenOneExists() {
		Set<Visit> visits = new LinkedHashSet<Visit>();
		Visit visit1 = new Visit();
		visit1.setDate(LocalDate.now());
		visit1.setId(321);
		visit1.setDescription("Checkup");
		visits.add(visit1);
		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.of(2000, 1, 1));
		visit2.setId(555);
		visit2.setDescription("Cleaning");
		visits.add(visit2);
		pet.setVisitsInternal(visits);
		List<Visit> list = pet.getVisits();
		assertTrue(list.contains(visit1));
		assertTrue(list.contains(visit2));
	}

	@Test
	void testGetVisitsWhenNoneExist() {
		List<Visit> list = pet.getVisits();
		assertTrue(list.isEmpty());
	}

}
