package com.eshope.admin.RestController;


import com.eshope.admin.Service.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShippingRateRestController {

    @Autowired
    private ShippingRateService shippingRateService;

    @PostMapping("/get_shipping_cost")
    public ResponseEntity getShippingCost(Integer productId,Integer countryId,String state) throws UsernameNotFoundException {
        try{
            float shippingCost = shippingRateService.calculateShippingCost(productId, countryId, state);
            return new ResponseEntity<>(shippingCost,HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

}
