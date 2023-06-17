package jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @Id @GeneratedValue
    private Long id;
    private String name;

    /** 계층형 카테고리 셀프 구성 */
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    /** @JoinTable : M:N에서 엔터티 분리 승격 어노테이션
     *  joinColumns와 inverseJoinColumns 옵션
     *  다이어그램 상 CATEGORY_ITEM 테이블은 먼저,
     *  CATEGORY 테이블을 기준으로 외래키를 갖게되고
     *  반대 방향 엔터티로 ITEM 테이블을 가지므로 순서는 아f래와 같다.
     *
     * */
    @ManyToMany
    @JoinTable(name = "CATEGORY_ITEM"
            , joinColumns = @JoinColumn(name = "CATEGORY_ID") // 현재 엔티티를 참조하는 외래키
            , inverseJoinColumns = @JoinColumn(name = "ITEM_ID") // 반대방향 엔터티를 참조하는 외래키
    )

    private List<Item> itemList = new ArrayList<>();

}
