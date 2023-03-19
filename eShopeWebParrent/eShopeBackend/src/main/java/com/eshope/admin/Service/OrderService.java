package com.eshope.admin.Service;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.Order.Order;
import com.eShope.common.entity.Order.OrderStatus;
import com.eShope.common.entity.Order.OrderTrack;
import com.eshope.admin.Repository.CountryRepository;
import com.eshope.admin.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    public static final int ORDERS_PER_PAGE=5;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SettingService settingService;

    @Autowired
    private CountryRepository countryRepository;


    public List<Order> listAllOrders(){
        return (List<Order>) orderRepository.findAll(Sort.by("firstName").ascending());
    }

    public Page<Order> listByPage(int pageNum,String sortField, String sortDir, String keyword){
       Sort sort=null;
        if("destination".equals(sortField))
            sort= Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
        else
            sort=Sort.by(sortField);
        sort=sortDir.equals("asc")?sort.ascending():sort.descending();

        Pageable pageable= PageRequest.of(pageNum-1,ORDERS_PER_PAGE,sort);

        if(keyword!=null){
            return orderRepository.findAll(keyword,pageable);
        }

        return orderRepository.findAll(pageable);
    }


    public Order getOrderById(Integer id) {
        try{
            return orderRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UsernameNotFoundException("Could not find any order with Id "+ id);
        }
    }

    public void delete(Integer id) throws UsernameNotFoundException{
        Long countById=orderRepository.countById(id);
        if(countById==null||countById==0){
            throw new UsernameNotFoundException("Could not found any order with such Id ");
        }
        orderRepository.deleteById(id);
    }

    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public void save(Order order) {
        List<OrderTrack> orderTracks=order.getOrderTracks();
        Date highestDate=orderTracks.get(0).getUpdatedTime();
        for(OrderTrack or:orderTracks){
            if(highestDate.compareTo(or.getUpdatedTime())<0){
                highestDate=or.getUpdatedTime();
                order.setOrderStatus(or.getStatus());
            }
        }

        Order existingOrder=orderRepository.findById(order.getId()).get();
        order.setOrderTime(existingOrder.getOrderTime());
        order.setCustomer(existingOrder.getCustomer());

        orderRepository.save(order);
    }

    public void updateStatus(Integer orderID,String status){
        Order existingOrder=orderRepository.findById(orderID).get();
        OrderStatus statusToUpdate=OrderStatus.valueOf(status);

        if(!existingOrder.hasStatus(statusToUpdate)){
            List<OrderTrack> orderTracks=existingOrder.getOrderTracks();

            OrderTrack track=new OrderTrack();
            track.setOrder(existingOrder);
            track.setStatus(statusToUpdate);
            track.setUpdatedTime(new Date());
            track.setNotes(statusToUpdate.defaultDescription());

            orderTracks.add(track);

            existingOrder.setOrderStatus(statusToUpdate);
            orderRepository.save(existingOrder);

        }




    }
}
