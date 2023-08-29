package com.eshope;

import com.eShope.common.entity.Address;
import com.eShope.common.entity.Country;
import com.eShope.common.entity.Customer;
import com.eshope.consumer.Repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void testAddNew(){
        Integer customerId=44;
        Integer countryId=106;

        Address newAddress=new Address();
        newAddress.setCustomer(new Customer(customerId));
        newAddress.setCountry(new Country(countryId));
        newAddress.setFirstName("Somay");
        newAddress.setLastName("Verma");
        newAddress.setPhoneNumber("2635421612");
        newAddress.setAddressLine1("1234712 nasjui asdvi");
        newAddress.setCity("Zirakpur");
        newAddress.setState("Punjab");
        newAddress.setPostalCode("10013");

        Address savedAddress=addressRepository.save(newAddress);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindByCustomer(){
        Integer customerId=5;
        List<Address> addressList=addressRepository.findByCustomer(new Customer(customerId));
        assertThat(addressList.size()).isGreaterThan(0);


    }

    @Test
    public void testFindByIdAndCustomer(){
        Integer customerId=5;
        Integer addressId=1;

        Address address=addressRepository.findByIdAndCustomer(addressId,customerId);

        assertThat(address).isNotNull();
    }

    @Test
    public void testUpdate(){
        Integer addressId=3;
//        String addressLine="126 nuas";
        boolean defaultAddress=true;
        Address address=addressRepository.findById(addressId).get();
        address.setDefaultForShipping(defaultAddress);

        Address updatedAddress=addressRepository.save(address);
        assertThat(updatedAddress.isDefaultForShipping()).isEqualTo(defaultAddress);
    }

    @Test
    public void testDeleteByIdAndCustomer(){
        Integer customerId=5;
        Integer addressId=1;

        addressRepository.deleteByIdAndCustomer(addressId,customerId);

        Address address=addressRepository.findByIdAndCustomer(addressId,customerId);
        assertThat(address).isNull();
    }

    @Test
    public void testSetDefault(){
        Integer addressId=7;
        addressRepository.setDefaultAddress(addressId);

        Address address=addressRepository.findById(addressId).get();
        assertThat(address.isDefaultForShipping()).isTrue();
    }

    @Test
    public void testSetNonDefaultAddresses(){
        Integer addressId=7;
        Integer customerId=44;
        addressRepository.setNonDefaultForOthers(addressId,customerId);
    }

    @Test
    public void testGetDefault(){
        Integer customerId=44;
        Address address=addressRepository.findDefaultAddressByCustomer(customerId);
        assertThat(address).isNotNull();
        System.out.println(address);
    }

}
