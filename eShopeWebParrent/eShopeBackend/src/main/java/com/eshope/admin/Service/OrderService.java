package com.eshope.admin.Service;

import com.eShope.common.entity.Order;
import com.eShope.common.entity.Setting;
import com.eShope.common.entity.User;
import com.eshope.admin.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    public static final int ORDERS_PER_PAGE=5;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SettingService settingService;


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
}
