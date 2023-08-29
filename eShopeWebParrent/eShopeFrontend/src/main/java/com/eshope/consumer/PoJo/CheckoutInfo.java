package com.eshope.consumer.PoJo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Slf4j
public class CheckoutInfo {

    private float productCost;
    private float productTotal;
    private float shippingCostTotal;
    private float paymentTotal;
    private int deliverDays;
    private boolean codSupported;

    public Date getDeliverDate(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,deliverDays);

        return calendar.getTime();
    }

    public String getPaymentTotalForPayPal(){
        DecimalFormat formatter=new DecimalFormat("###.##");
        return formatter.format(paymentTotal);

    }
}
