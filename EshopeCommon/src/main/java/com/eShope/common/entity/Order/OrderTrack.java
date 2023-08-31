package com.eShope.common.entity.Order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
@Getter
@Setter
@Table(name = "order_track")
public class OrderTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 256)
    private String notes;

    private Date updatedTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 45,nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @Transient
    public String getUpdatedTimeOnForm() {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault());
//        dateFormatter.setTimeZone(java.util.TimeZone.getTimeZone("Asia/India"));

        return dateFormatter.format(this.updatedTime);
//
    }

    public void setUpdatedTimeOnForm(String dateString) throws ParseException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        //dateFormatter.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Istanbul"));
        try {
            this.updatedTime = dateFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
