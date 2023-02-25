package com.eShope.common.entity.Order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
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

}
