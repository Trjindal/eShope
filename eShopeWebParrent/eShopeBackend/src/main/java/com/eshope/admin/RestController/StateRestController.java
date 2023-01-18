package com.eshope.admin.RestController;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.State;
import com.eshope.admin.DTO.StateDTO;
import com.eshope.admin.Repository.StateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class StateRestController {

    @Autowired
    StateRepository stateRepository;

    @GetMapping("/states/listByCountry/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId){
        List<State> listStates=stateRepository.findByCountryOrderByNameAsc(new Country(countryId));
        List<StateDTO> result=new ArrayList<>();

        for (State state:listStates){
            result.add(new StateDTO(state.getId(),state.getName()));
        }
        return result;
    }

    @PostMapping("/states/save")
    public String save(@RequestBody State state){
        State savedState=stateRepository.save(state);
        return String.valueOf(savedState.getId());
    }

    @GetMapping("/states/delete/{id}")
    public void delete(@PathVariable("id") Integer id){
        stateRepository.deleteById(id);
    }
    @PostMapping("/states/check_unique")
    @ResponseBody
    public String checkUnique(@RequestBody Map<String,String> data) {

        String name = data.get("name");

        log.info("StateRestController | checkUnique is called");

        log.info("StateRestController | checkUnique | name : " + name);

        State countryByName = stateRepository.findByName(name);
        boolean isCreatingNew = (countryByName != null ? true : false);
        log.error(String.valueOf(isCreatingNew));
        if (isCreatingNew) {
            if (countryByName != null) return "Duplicate";
        } else {
            if (countryByName != null && countryByName.getId() != null) {
                return "Duplicate";
            }
        }
        log.error("OK");
        return "OK";
    }
}
