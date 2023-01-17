package com.eshope.admin.RestController;

import com.eShope.common.entity.Country;
import com.eshope.admin.Repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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

    @GetMapping("/countries/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        try{
        countryRepository.deleteById(id);}
        catch (Exception ex){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return  new ResponseEntity(HttpStatus.OK);
    }

}
