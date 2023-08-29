package com.eshope;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.ShippingRate;
import com.eshope.consumer.Repository.ShippingRateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ShippingRateRepositoryTest {

    @Autowired
    private ShippingRateRepository shippingRateRepository;

    @Test
    public void testFindByCountryAndState(){
        Country india=new Country(106);
        String state="Punjab";
        ShippingRate shippingRate=shippingRateRepository.findByCountryAndState(india,state);

        assertThat(shippingRate).isNotNull();

    }
}
