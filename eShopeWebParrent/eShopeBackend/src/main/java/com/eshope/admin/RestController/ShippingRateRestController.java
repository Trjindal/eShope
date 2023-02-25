package com.eshope.admin.RestController;


import com.eshope.admin.Service.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShippingRateRestController {

    @Autowired
    private ShippingRateService shippingRateService;

    @PostMapping("/get_shipping_cost")
    public String getShippingCost(Integer productId,Integer countryId,String state) throws UsernameNotFoundException {
        float shippingCost= shippingRateService.calculateShippingCost(productId,countryId,state);
        return String.valueOf(shippingCost);
    }

}
