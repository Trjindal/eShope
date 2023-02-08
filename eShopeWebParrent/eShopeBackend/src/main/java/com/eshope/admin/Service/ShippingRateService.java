package com.eshope.admin.Service;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.ShippingRate;
import com.eShope.common.entity.User;
import com.eshope.admin.Repository.CountryRepository;
import com.eshope.admin.Repository.ShippingRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ShippingRateService {

    public static final int SHIPPING_RATES_PER_PAGE=10;

    @Autowired
    private ShippingRateRepository shippingRateRepository;

    @Autowired
    private CountryRepository countryRepository;

    public Page<ShippingRate> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort= Sort.by(sortField);
        sort=sortDir.equals("asc")?sort.ascending():sort.descending();

        Pageable pageable= PageRequest.of(pageNum-1,SHIPPING_RATES_PER_PAGE,sort);

        if(keyword!=null){
            return shippingRateRepository.findAll(keyword,pageable);
        }

        return shippingRateRepository.findAll(pageable);
    }

    public List<Country> listAllCountries(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    public ShippingRate saveShippingRate(ShippingRate shippingRate) {
        return shippingRateRepository.save(shippingRate);
    }

    public boolean isUnique(Country country, String state) {
        return shippingRateRepository.findByCountryAndState(country.getId(),state)==null?true:false;
    }

    public void delete(Integer id) throws UsernameNotFoundException {
        Long countById=shippingRateRepository.countById(id);
        if(countById==null||countById==0){
            throw new UsernameNotFoundException("Could not found any Shipping Address with Id "+id);
        }
        shippingRateRepository.deleteById(id);
    }

    public void updateShippingAddressEnabledStatus(Integer id, boolean enabled) {
        Long countById=shippingRateRepository.countById(id);
        if(countById==null||countById==0){
            throw new UsernameNotFoundException("Could not found any Shipping Address with Id "+id);
        }
        shippingRateRepository.updateCodSupport(id,enabled);
    }

    public ShippingRate getShippingById(Integer id) {
        try{
            return shippingRateRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UsernameNotFoundException("Could not find any Shipping Address with Id"+ id);
        }
    }
}
