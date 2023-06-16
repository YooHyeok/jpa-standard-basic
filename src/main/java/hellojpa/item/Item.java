package hellojpa.item;

import javax.persistence.*;

/**
 * 상속관계 엔티티 정규화 Join 전략 설정(Album, Movie, Book)
 * @Ingeritance
 * 옵션 : strategy = InheritanceType.JOINED
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 부모 item 자식 album, movie, book 테이블이 각각 정규화된다.
@DiscriminatorColumn(name = "DIS_TYPE") // 기본 컬럼명은 Dtype이지만 name 속성으로 컬럼명을 지정할 수 있다.
public class Item {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private int price;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
