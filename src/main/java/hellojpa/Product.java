package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * M:N 연관관계 예시 엔터티 (Member 엔터티의 productList와 매핑)
 */
@Entity
public class Product {

    @Id @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;

    private String name;

    /** M:N 양방향 연관관계 객체 */
    @ManyToMany(mappedBy = "productList")
    public List<Member> memberList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
