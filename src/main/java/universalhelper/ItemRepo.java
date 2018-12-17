package universalhelper;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepo extends CrudRepository<Item, Integer> {
    List<Item> findById(Long Id);

    List<Present>
    findByMinPriceLessThanEqualAndMaxPriceGreaterThanEqualAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqualAndGender
            (Integer minPrice, Integer maxPrice,
             Integer minAge, Integer maxAge,
             Character gender);
}
