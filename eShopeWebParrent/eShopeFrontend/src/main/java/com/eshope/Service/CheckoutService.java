package com.eshope.Service;

import com.eShope.common.entity.CartItem;
import com.eShope.common.entity.Product;
import com.eShope.common.entity.ShippingRate;
import com.eshope.PoJo.CheckoutInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {

//    THIS IS USED BY FEDEX
    public static final int DIM_DIVISOR=139;

    public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate){

        CheckoutInfo checkoutInfo=new CheckoutInfo();
        float productCost= calculateProductCost(cartItems);
        float productTotal=calculateProductTotal(cartItems);
        float shippingCostTotal=calculateShippingCost(cartItems,shippingRate);
        float paymentTotal=productTotal+shippingCostTotal;

        checkoutInfo.setProductCost(productCost);
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setPaymentTotal(paymentTotal);
        checkoutInfo.setDeliverDays(shippingRate.getDays());
        checkoutInfo.setCodSupported(shippingRate.isCodSupported());
        checkoutInfo.setShippingCostTotal(shippingCostTotal);

        return checkoutInfo;
    }

    private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {

        float shippingCostTotal=0.0f;

        for(CartItem item:cartItems){
            Product product=item.getProduct();
            float dimWeight=(product.getLength()*product.getWidth()*product.getHeight())/DIM_DIVISOR;
            float finalWeight=product.getWeight()>dimWeight?product.getWeight():dimWeight;
            float shippingCost=finalWeight*item.getQuantity()*shippingRate.getRate();
            item.setShippingCost(shippingCost);

            shippingCostTotal+=shippingCost;
        }

        return  shippingCostTotal;
    }

    private float calculateProductTotal(List<CartItem> cartItems){

        float total=0.0f;
        for(CartItem item:cartItems){
            total+=item.getSubTotal();
        }

        return total;
    }

    private float calculateProductCost(List<CartItem> cartItems){
        float cost=0.0f;

        for (CartItem cartItem:cartItems){
            cost+=cartItem.getQuantity()*cartItem.getProduct().getCost();
        }
        return cost;
    }

}
