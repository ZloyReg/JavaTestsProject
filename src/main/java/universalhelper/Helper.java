package universalhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/helper") // This means URL's start with /demo (after Application path)
public class Helper {
    @Autowired // This means to get the bean called itemRepo
    // Which is auto-generated by Spring, we will use it to handle the data

    private ItemRepo itemRepo;



    @PutMapping(path="/addItem") // Map ONLY GET Requests
    public @ResponseBody
    String addNewItem (@RequestParam String name
            , @RequestParam String description) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        getItemRepo().save(item);
        return "Saved";
    }

    @PutMapping(path="/addPresent") // Map ONLY GET Requests
    public @ResponseBody
    String addNewPresent (@RequestParam String name
            , @RequestParam String description, @RequestParam Integer minPrice
            , @RequestParam Integer maxPrice, @RequestParam Integer minAge
            , @RequestParam Integer maxAge, @RequestParam Character gender) {

        Present present = new Present();
        present.setName(name);
        present.setDescription(description);
        present.setMinPrice(minPrice);
        present.setMaxPrice(maxPrice);
        present.setMinAge(minAge);
        present.setMaxAge(maxAge);
        present.setGender(gender);
        getItemRepo().save(present);
        return "Saved";
    }
    @GetMapping(path="/itemsAll")
    public @ResponseBody Iterable<? extends Item> getAllItems() {
        // This returns a JSON or XML with the items
        return getItemRepo().findAll();
    }

    @GetMapping(path="/presentsFiltered")
    public @ResponseBody
    Iterable<? extends Item> getFilteredPresentList(@RequestParam Integer minPrice
            , @RequestParam Integer maxPrice, @RequestParam Integer minAge
            , @RequestParam Integer maxAge, @RequestParam Character gender) {
        if (minAge == null){
            minAge = 150;
        }
        if (maxAge == null){
            maxAge = 0;
        }
        if (gender == null){
            gender = 'a';
        }
        if (minPrice != null && maxPrice != null){
            return getItemRepo().findByMinPriceLessThanEqualAndMaxPriceGreaterThanEqualAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqualAndGender
                    (minPrice, maxPrice, minAge, maxAge,
                            gender);
        }
        else return getItemRepo().findAll();
    }

   @DeleteMapping(path ="/itemsClear")
    public String deleteAllItems() {
       getItemRepo().deleteAll();
       return "Database is cleared";
   }

    @DeleteMapping(path ="/itemClearById")
    public String deleteItemById(@RequestParam Integer id) {
        if (id != null){
            getItemRepo().deleteById(id);
            return "Item is deleted, id = " + id;
        }
        return "Item ID is required, but not set";
    }

    public ItemRepo getItemRepo() {
        return itemRepo;
    }

    public void setItemRepo(ItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }
}