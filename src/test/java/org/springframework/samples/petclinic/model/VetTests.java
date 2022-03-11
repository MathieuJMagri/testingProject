/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 */
class VetTests {

	private Vet vet = new Vet();

	@BeforeEach
	void setup() {
		vet.setSpecialtiesInternal(null);
		vet.setFirstName("Zaphod");
		vet.setLastName("Beeblebrox");
		vet.setId(123);
	}

	@Test
	void testSerialization() {
		Vet other = (Vet) SerializationUtils.deserialize(SerializationUtils.serialize(vet));
		assertThat(other.getFirstName()).isEqualTo(vet.getFirstName());
		assertThat(other.getLastName()).isEqualTo(vet.getLastName());
		assertThat(other.getId()).isEqualTo(vet.getId());
	}

	@Test
	void testAddSpecialtyToEmptySet() {
		Specialty specialty = new Specialty();
		specialty.setName("Surgery");
		vet.addSpecialty(specialty);
		assertEquals("[" + specialty.getName() + "]", vet.getSpecialtiesInternal().toString());
	}

	@Test
	void testAddSpecialtyToExistingSet() {
		Set<Specialty> specialties = new HashSet<Specialty>();
		Specialty specialty1 = new Specialty();
		specialty1.setName("Surgery");
		specialties.add(specialty1);
		vet.setSpecialtiesInternal(specialties);
		Specialty specialty2 = new Specialty();
		specialty2.setName("Examination");
		vet.addSpecialty(specialty2);
		assertTrue(vet.getSpecialtiesInternal().contains(specialty1));
		assertTrue(vet.getSpecialtiesInternal().contains(specialty2));
	}

	@Test
	void testGetNrOfSpecialties() {
		Set<Specialty> specialties = new HashSet<Specialty>();
		Specialty specialty1 = new Specialty();
		specialty1.setName("Surgery");
		specialties.add(specialty1);
		Specialty specialty2 = new Specialty();
		specialty2.setName("Examination");
		specialties.add(specialty2);
		vet.setSpecialtiesInternal(specialties);
		assertEquals(2, vet.getNrOfSpecialties());
	}

	@Test
	void testGetSpecialtiesWhenOneExists() {
		Set<Specialty> specialties = new HashSet<Specialty>();
		Specialty specialty1 = new Specialty();
		specialty1.setName("Surgery");
		specialties.add(specialty1);
		Specialty specialty2 = new Specialty();
		specialty2.setName("Examination");
		specialties.add(specialty2);
		vet.setSpecialtiesInternal(specialties);
		List<Specialty> list = vet.getSpecialties();
		assertTrue(list.contains(specialty1));
		assertTrue(list.contains(specialty2));
	}

	@Test
	void testGetSpecialtiesWhenNoneExist() {
		List<Specialty> list = vet.getSpecialties();
		assertTrue(list.isEmpty());
	}

}
