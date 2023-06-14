package jpabook.jpashop.domain;

import javax.persistence.*;

//@Entity
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ITEM_ID")
    private Long id;
    @Column
    private String name;
    @Column
    private int price;
    @Column
    private int stockQuantity;
}
