package jpql.domain;

import javax.persistence.*;

@Entity
public class Member {

    @Id @GeneratedValue
    public Long id;
    public String username;
    public int age;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    public Team team;

    /* Getter & Setter */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
