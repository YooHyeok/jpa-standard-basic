package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity //JPA 첫로딩시 Entity로 관리할수 있도록 인식
public class Member {
    @Id
    private Long id;
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
