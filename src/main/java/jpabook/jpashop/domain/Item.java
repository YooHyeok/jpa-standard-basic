package jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
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

    @ManyToMany(mappedBy = "itemList")
    private List<Category> categoryList = new ArrayList<>();

}
