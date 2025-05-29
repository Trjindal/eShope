package com.eshope.admin;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.State;
import com.eshope.admin.Repository.StateRepository;
import org.apache.poi.ss.formula.functions.Count;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTest {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCrateStatesInIndia(){
        Integer countryId=2;
        Country country=testEntityManager.find(Country.class,countryId);

//        State state=stateRepository.save(new State("Karnatka",country));
//        State state=stateRepository.save(new State("Punjab",country));
//        State state=stateRepository.save(new State("Uttar Pradesh",country));
        State state=stateRepository.save(new State("Haryana",country));
        assertThat(state).isNotNull();
        assertThat(state.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateStatesInUs(){
        Integer countryId=3;
        Country country=testEntityManager.find(Country.class,countryId);

//        State state=stateRepository.save(new State("California",country));
//        State state=stateRepository.save(new State("Texas",country));
        State state=stateRepository.save(new State("New York Pradesh",country));
//        State state=stateRepository.save(new State("California",country));
        assertThat(state).isNotNull();
        assertThat(state.getId()).isGreaterThan(0);
    }

    @Test
    public void testUpdateState(){
        Integer stateId=3;
        String stateName="Tamil Nadu";
        State state=stateRepository.findById(stateId).get();

        state.setName(stateName);
        State savedState=stateRepository.save(state);

        assertThat(savedState.getName()).isEqualTo(stateName);
    }

    @Test
    public void testListStatesByCountry(){
        Integer countryId=2;
        Country country=testEntityManager.find(Country.class,countryId);
        List<State> listStates=stateRepository.findByCountryOrderByNameAsc(country);

        listStates.forEach(System.out::println);
        assertThat(listStates.size()).isEqualTo(4);
    }

    @Test
    public void testDeleteState(){
        Integer id=1;
        stateRepository.deleteById(id);

        Optional<State> findById=stateRepository.findById(id);
        assertThat(findById.isEmpty());
    }
}
