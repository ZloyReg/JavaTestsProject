package unitTests;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import universalhelper.Helper;
import universalhelper.Item;
import universalhelper.ItemRepo;
import universalhelper.Present;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class HelperControllerTest {

    @InjectMocks
    Helper helper = new Helper();

    @Mock
    ItemRepo itemRepo;

    MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        helper.setItemRepo(itemRepo);
        mockMvc = MockMvcBuilders.standaloneSetup(helper).build();

    }

    @Test
    public void testAddItem() {
        Item item = new Item();
        item.setName("TestI");
        item.setDescription("Test item");

        ArgumentCaptor<Item> argumentCaptor = ArgumentCaptor.forClass(Item.class);

        String returnValue = helper.addNewItem(item.getName(), item.getDescription());

        verify(itemRepo, times(1)).save(argumentCaptor.capture());

        assertEquals("Saved", returnValue);
        assertEquals(item.getDescription(), argumentCaptor.getValue().getDescription());
        assertEquals(item.getName(), argumentCaptor.getValue().getName());

//        mockMvc.perform(put("localhost:8080/helper/addItem?name=Vlad&description=DESC"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().string("Saved"));
    }

    @Test
    public void testAddPresent() {
        Present present = new Present();
        present.setName("TestI");
        present.setDescription("Test present");
        present.setMinPrice(1);
        present.setMaxPrice(10);
        present.setMinAge(1);
        present.setMaxAge(20);
        present.setGender('f');

        ArgumentCaptor<Present> argumentCaptor = ArgumentCaptor.forClass(Present.class);

        String returnValue = helper.addNewPresent(present.getName(), present.getDescription(),
                present.getMinPrice(), present.getMaxPrice(), present.getMinAge(),
                present.getMaxAge(), present.getGender());

        verify(itemRepo, times(1)).save(argumentCaptor.capture());

        assertEquals("Saved", returnValue);
        assertEquals(present.getDescription(), argumentCaptor.getValue().getDescription());
        assertEquals(present.getName(), argumentCaptor.getValue().getName());
        assertEquals(present.getMinPrice(), argumentCaptor.getValue().getMinPrice());
        assertEquals(present.getMaxPrice(), argumentCaptor.getValue().getMaxPrice());
        assertEquals(present.getMinAge(), argumentCaptor.getValue().getMinAge());
        assertEquals(present.getMaxAge(), argumentCaptor.getValue().getMaxAge());
        assertEquals(present.getGender(), argumentCaptor.getValue().getGender());
    }

    @Test
    public void testGetAllItems() {
        helper.getAllItems();
        verify(itemRepo, times(1)).findAll();
    }

    @Test
    public void testGetFilteredPresentList() {

        Integer minPrice = 1;
        Integer maxPrice = 10;
        Integer minAge = 1;
        Integer maxAge = 20;
        Character gender = 'f';

        helper.getFilteredPresentList(minPrice, maxPrice, minAge, maxAge, gender);

        verify(itemRepo, times(1))
                .findByMinPriceLessThanEqualAndMaxPriceGreaterThanEqualAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqualAndGender
                        (minPrice, maxPrice, minAge, maxAge, gender);
    }

    @Test
    public void testGetFilteredPresentListNoAgeAndGender() {
        Integer minPrice = 1;
        Integer maxPrice = 10;
        Integer minAge = null;
        Integer maxAge = null;
        Character gender = null;
        helper.getFilteredPresentList(minPrice, maxPrice, minAge, maxAge, gender);

        verify(itemRepo, times(1))
                .findByMinPriceLessThanEqualAndMaxPriceGreaterThanEqualAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqualAndGender
                        (minPrice, maxPrice, 150, 0, 'a');
    }

    @Test
    public void testGetFilteredPresentListNoMinPrice() {
        Integer minPrice = null;
        Integer maxPrice = 10;
        Integer minAge = 1;
        Integer maxAge = 20;
        Character gender = 'f';

        helper.getFilteredPresentList(minPrice, maxPrice, minAge, maxAge, gender);

        verify(itemRepo, times(1))
                .findAll();

    }

    @Test
    public void testGetFilteredPresentListNoMaxPrice() {
        Integer minPrice = 1;
        Integer maxPrice = null;
        Integer minAge = 1;
        Integer maxAge = 20;
        Character gender = 'f';

        helper.getFilteredPresentList(minPrice, maxPrice, minAge, maxAge, gender);

        verify(itemRepo, times(1))
                .findAll();

    }

    @Test
    public void testGetFilteredPresentListNoPrices() {
        Integer minPrice = null;
        Integer maxPrice = null;
        Integer minAge = 1;
        Integer maxAge = 20;
        Character gender = 'f';

        helper.getFilteredPresentList(minPrice, maxPrice, minAge, maxAge, gender);

        verify(itemRepo, times(1))
                .findAll();

    }

    @Test
    public void testDeleteAllItems() {
        String returnValue = helper.deleteAllItems();
        verify(itemRepo, times(1)).deleteAll();
        assertEquals("Database is cleared", returnValue);
    }

    @Test
    public void testDeleteItemById() {
        Integer id = 1;
        String returnValue = helper.deleteItemById(id);
        verify(itemRepo, times(1)).deleteById(id);
        assertEquals("Item is deleted, id = " + id, returnValue);
    }

    @Test
    public void testDeleteItemWithNullId() {
        Integer id = null;
        String returnValue = helper.deleteItemById(id);
        assertEquals("Item ID is required, but not set", returnValue);

    }
}
