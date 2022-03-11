package org.springframework.samples.petclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.VisitRepository;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Test class for the {@link OwnerController}
 */
@WebMvcTest(OwnerController.class)
public class OwnerControllerTest {
	// Test naming convention used: test[MethodName][AnySpecialConditions]

	// attribute name for returned object
	private static final String entityName = "owner";

	// class params
	private static final String lastName = "lastName";
	private static final String firstName = "firstName";
	private static final String address = "address";
	private static final String city = "city";
	private static final String telephone = "telephone";
	private static final String pets = "telephone";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OwnerRepository ownerRepository;

	@MockBean
	private VisitRepository visitRepository;

	private Owner owner;

	@BeforeEach
	void setupTestData() {
		// owner test values
		int ownerId = 1234;
		String ownerLastName = "Appleseed";
		String ownerFirstName = "John";
		String ownerAddress = "3429 Rue Saint Famille";
		String ownerCity = "Montreal";
		String ownerTelephone = "5144566789";

		// pet test values
		int petId = 9876;
		String petName = "Panther";
		String petTypeName = "testPetType";

		// initializing test data
		owner = new Owner();
		owner.setId(ownerId);
		owner.setLastName(ownerLastName);
		owner.setFirstName(ownerFirstName);
		owner.setAddress(ownerAddress);
		owner.setCity(ownerCity);
		owner.setTelephone(ownerTelephone);

		Pet pet = new Pet();
		pet.setId(petId);
		pet.setName(petName);
		pet.setOwner(owner);

		Set<Pet> pets = new HashSet<>();
		pets.add(pet);

		owner.setPetsInternal(pets);
	}

	@Test
	public void testInitCreationForm() throws Exception {
		// Arrange
		String endpoint = "/owners/new";
		String expectedViewName = "owners/createOrUpdateOwnerForm";

		// Act / Assert
		mockMvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(model().attributeExists(entityName)).andExpect(view().name(expectedViewName));
	}

	@Test
	void testProcessCreationFormWithErrors() throws Exception {
		// Arrange
		String endpoint = "/owners/new";
		String expectedViewName = "owners/createOrUpdateOwnerForm";

		// Act / Assert
		mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).param(firstName, owner.getFirstName())
				.param(address, owner.getAddress())).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors(entityName))
				.andExpect(model().attributeHasFieldErrors(entityName, lastName))
				.andExpect(model().attributeHasFieldErrors(entityName, city))
				.andExpect(model().attributeHasFieldErrors(entityName, telephone))
				.andExpect(view().name(expectedViewName));
	}

	@Test
	void testProcessCreationFormWithNoErrors() throws Exception {
		// Arrange
		String endpoint = "/owners/new";
		String expectedViewName = "redirect:/owners/null";

		// Act / Assert
		mockMvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).param(lastName, owner.getLastName())
				.param(firstName, owner.getFirstName()).param(address, owner.getAddress()).param(city, owner.getCity())
				.param(telephone, owner.getTelephone())).andExpect(status().is3xxRedirection())
				.andExpect(view().name(expectedViewName));
		;
	}

	@Test
	void testInitFindForm() throws Exception {
		// Arrange
		String endpoint = "/owners/find";
		String expectedViewName = "owners/findOwners";

		// Act / Assert
		mockMvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(model().attributeExists(entityName)).andExpect(view().name("owners/findOwners"));
	}

	@Test
	void testProcessFindFormWithNoLastNameAndNoResults() throws Exception {
		// Arrange
		String endpoint = "/owners";
		String expectedViewName = "owners/findOwners";

		List<Owner> owners = new ArrayList<>();
		Set<Owner> tasks = new HashSet<Owner>(owners);

		when(this.ownerRepository.findByLastName("")).thenReturn(tasks);

		mockMvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors(entityName, lastName))
				.andExpect(model().attributeHasFieldErrorCode(entityName, lastName, "notFound"))
				.andExpect(view().name(expectedViewName));
	}

	@Test
	void testProcessFindFormWithNoResults() throws Exception {
		// Arrange
		String endpoint = "/owners";
		String expectedViewName = "owners/findOwners";
		List<Owner> owners = new ArrayList<>();
		Set<Owner> tasks = new HashSet<Owner>(owners);

		when(this.ownerRepository.findByLastName("Does not exist")).thenReturn(tasks);

		mockMvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON).param(lastName, "Does not exist"))
				.andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors(entityName, lastName))
				.andExpect(model().attributeHasFieldErrorCode(entityName, lastName, "notFound"))
				.andExpect(view().name(expectedViewName));
	}

	@Test
	void testProcessFindFormWithOneResult() throws Exception {
		// Arrange
		String endpoint = "/owners";
		String expectedViewName = "redirect:/owners/" + owner.getId();

		Set<Owner> owners = new HashSet<>();
		owners.add(owner);

		when(this.ownerRepository.findByLastName(owner.getLastName())).thenReturn(owners);

		// Act / Assert
		mockMvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON).param(lastName, owner.getLastName()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name(expectedViewName));
	}

	@Test
	void testProcessFindFormWithMultipleResults() throws Exception {
		// Arrange
		String endpoint = "/owners";
		String expectedViewName = "owners/ownersList";

		Set<Owner> owners = new HashSet<>();
		owners.add(owner);

		Owner ownerTwo = new Owner();
		ownerTwo.setLastName(owner.getLastName());
		owners.add(ownerTwo);

		when(this.ownerRepository.findByLastName(owner.getLastName())).thenReturn(owners);

		// Act / Assert
		mockMvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON).param(lastName, owner.getLastName()))
				.andExpect(status().isOk()).andExpect(model().attributeExists("selections"))
				.andExpect(view().name(expectedViewName));
	}

	@Test
	void testInitUpdateOwnerForm() throws Exception {
		// Arrange
		String endpoint = "/owners/{ownerId}/edit";
		String expectedViewName = "owners/createOrUpdateOwnerForm";

		when(this.ownerRepository.findById(owner.getId())).thenReturn(owner);

		// Act / Assert
		mockMvc.perform(get(endpoint, owner.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(model().attributeExists(entityName))
				.andExpect(model().attribute(entityName, hasProperty(lastName, is(owner.getLastName()))))
				.andExpect(model().attribute(entityName, hasProperty(firstName, is(owner.getFirstName()))))
				.andExpect(model().attribute(entityName, hasProperty(address, is(owner.getAddress()))))
				.andExpect(model().attribute(entityName, hasProperty(city, is(owner.getCity()))))
				.andExpect(model().attribute(entityName, hasProperty(telephone, is(owner.getTelephone()))))
				.andExpect(view().name(expectedViewName));
	}

	@Test
	void testProcessUpdateOwnerFormWithErrors() throws Exception {
		// Arrange
		String endpoint = "/owners/{ownerId}/edit";
		String expectedViewName = "owners/createOrUpdateOwnerForm";

		// Act / Assert
		mockMvc.perform(post(endpoint, owner.getId()).contentType(MediaType.APPLICATION_JSON)
				.param(firstName, owner.getFirstName()).param(address, owner.getAddress())
				.param(city, owner.getLastName())).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors(entityName))
				.andExpect(model().attributeHasFieldErrors(entityName, telephone))
				.andExpect(model().attributeHasFieldErrors(entityName, lastName))
				.andExpect(view().name(expectedViewName));
	}

	@Test
	void testProcessUpdateOwnerFormWithNoErrors() throws Exception {
		// Arrange
		String endpoint = "/owners/{ownerId}/edit";
		String expectedViewName = "redirect:/owners/{ownerId}";

		// Act / Assert
		mockMvc.perform(post(endpoint, owner.getId()).contentType(MediaType.APPLICATION_JSON)
				.param(lastName, owner.getLastName()).param(firstName, owner.getFirstName())
				.param(address, owner.getAddress()).param(city, owner.getCity()).param(telephone, owner.getTelephone()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name(expectedViewName));
	}

	@Test
	void testShowOwner() throws Exception {
		// Arrange
		String endpoint = "/owners/{ownerId}";
		String expectedViewName = "owners/ownerDetails";

		when(this.ownerRepository.findById(owner.getId())).thenReturn(owner);

		// Act / Assert
		mockMvc.perform(get(endpoint, owner.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(model().attribute(entityName, hasProperty(lastName, is(owner.getLastName()))))
				.andExpect(model().attribute(entityName, hasProperty(firstName, is(owner.getFirstName()))))
				.andExpect(model().attribute(entityName, hasProperty(address, is(owner.getAddress()))))
				.andExpect(model().attribute(entityName, hasProperty(city, is(owner.getCity()))))
				.andExpect(model().attribute(entityName, hasProperty(telephone, is(owner.getTelephone()))))
				.andExpect(model().attribute(entityName, hasProperty(pets, not(empty()))))
				.andExpect(view().name(expectedViewName));
	}

}
