package com.eshope.admin.Repository;

import com.eShope.common.entity.ShippingRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

public interface ShippingRateRepository extends PagingAndSortingRepository<ShippingRate,Integer> {

    @Query("SELECT sr FROM ShippingRate sr WHERE sr.country.name LIKE %?1% OR sr.state LIKE %?1%")
    public Page<ShippingRate> findAll(String keyword, Pageable pageable);

    @Modifying
    @Transactional
    @Query("Update ShippingRate sr SET sr.codSupported=?2 where sr.id=?1")
    public void updateCodSupport(Integer id,boolean enabled);

    @Query("SELECT sr FROM ShippingRate sr WHERE sr.country.id=?1 AND sr.state =?2")
    public ShippingRate findByCountryAndState(Integer countryId,String state);

    public Long countById(Integer id);

}
