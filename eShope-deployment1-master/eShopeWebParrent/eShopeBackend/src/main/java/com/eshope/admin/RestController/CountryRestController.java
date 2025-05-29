package com.eshope.admin.RestController;

import com.eShope.common.entity.Country;
import com.eshope.admin.Repository.CountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class CountryRestController {

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping("/countries/list")
    public List<Country> listAll(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    @PostMapping("countries/save")
    public String save(@RequestBody Country country){
        Country savedCountry=countryRepository.save(country);
        return String.valueOf(savedCountry.getId());
    }

    @DeleteMapping("/countries/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        try{
        countryRepository.deleteById(id);}
        catch (Exception ex){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/countries/check_unique")
    @ResponseBody
    public String checkUnique(@RequestBody Map<String,String> data) {

        String name = data.get("name");

        log.info("CountryRestController | checkUnique is called");

        log.info("CountryRestController | checkUnique | name : " + name);

        Country countryByName = countryRepository.findByName(name);
        boolean isCreatingNew = (countryByName != null ? true : false);
        log.error(String.valueOf(isCreatingNew));
        if (isCreatingNew) {
            if (countryByName != null){
                log.error("Duplicate");
                return "Duplicate";
            }
        } else {
            if (countryByName != null && countryByName.getId()>0) {
                return "Duplicate";
            }
        }

        return "OK";
    }

}
