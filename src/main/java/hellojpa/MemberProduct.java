package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * M:N 연관관계에서 (M:1/1:M)으로 분리할 중간 테이블 엔터티 승격
 */
@Entity
public class MemberProduct {

    @Id @GeneratedValue
    private Long id;

    /** Member 엔터티와 연관관계 */
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    /** Product 엔터티와 연관관계 */
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private int count;
    private int price;
    private LocalDateTime orderDateTime;

}
