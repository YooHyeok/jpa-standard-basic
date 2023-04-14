package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity //JPA 첫로딩시 Entity로 관리할수 있도록 인식
//@Table(name = "USER") // DB에 Member라는 이름이 아닌 User라는 이름의 테이블을 설정한다면 해당 어노테이션으로 지정한다. 생략시 엔티티 클래스명으로 테이블이 관리됨.
public class Member {
    @Id
    private Long id;
//    @Column(name = "username") //DB 컬럼이 username이라면 해당 어노테이션을 통해서 Entity가 관리된다. (생략시 필드명으로 컬럼명이 관리됨)
    private String name;

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
