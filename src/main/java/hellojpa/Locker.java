package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 1:1 연관관계 예시 엔터티 (Member 엔터티의 Locker와 매핑된다.)
 */
//@Entity
public class Locker {

    @Id
    @GeneratedValue
    @Column(name = "LOCKER_ID")
    private Long id;
    private String name;

    /** 1:1 연관관계 매핑 Locker 엔터티의 locker와 매핑된다.*/
    @OneToOne(mappedBy = "locker")
    private Member member;
}