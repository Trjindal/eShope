package com.eshope.Service;


import com.eShope.common.entity.CartItem;
import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Product;
import com.eshope.Repository.ProductRepository;
import com.eshope.Repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerService customerService;

    public Integer addProduct(Integer productId, Integer quantity, Customer customer){

        Integer updatedQuantity=quantity;
        Product product=new Product(productId);
        CartItem cartItem =shoppingCartRepository.findByCustomerAndProduct(customer,product);
        if(cartItem!=null){
            updatedQuantity=cartItem.getQuantity()+quantity;
            if(updatedQuantity>5){
                Integer balance=5-cartItem.getQuantity();
                throw new UsernameNotFoundException("The maximum allowed quantity per product is 5. There are already "+ cartItem.getQuantity()+" quantity(s) in your shopping cart. Only "+balance +" additional quantity(s) can be added.");
            }
        }else{
            cartItem=new CartItem();
            cartItem.setProduct(product);
            cartItem.setCustomer(customer);
        }
        cartItem.setQuantity(updatedQuantity);
        shoppingCartRepository.save(cartItem);

        return updatedQuantity;
    }

    public List<CartItem> listCartItems(Customer customer){
        return shoppingCartRepository.findByCustomer(customer);
    }

    public float updateQuantity(Integer productId,Integer quantity,Customer customer){
        shoppingCartRepository.updateQuantity(quantity, customer.getId(), productId);
        Product product=productRepository.findById(productId).get();
        float subtotal=product.getDiscountPrice()*quantity;
        return subtotal;
    }

    public void removeProduct(Integer productId,Customer customer){
        shoppingCartRepository.deleteByCustomerAndProduct(customer.getId(),productId);
    }

}
