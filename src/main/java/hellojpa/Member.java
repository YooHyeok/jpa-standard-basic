package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity //JPA 첫로딩시 Entity로 관리할수 있도록 인식
//@Table(name = "USER") // DB에 Member라는 이름이 아닌 User라는 이름의 테이블을 설정한다면 해당 어노테이션으로 지정한다. 생략시 엔티티 클래스명으로 테이블이 관리됨.
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "USERNAME") //DB 컬럼이 username이라면 해당 어노테이션을 통해서 Entity가 관리된다. (생략시 필드명으로 컬럼명이 관리됨)
    private String username;

    /** 1:1 연관관계 Locker 엔터티의 LOCKER_ID와 매핑된다. */
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    @ManyToOne//Member 입장에서 M Team입장에서 1이다
    @JoinColumn(name = "TEAM_ID") // TEAM 엔터티의 PK TEAM_ID를 기준으로 조인 (Member엔터티에 TEAM_ID 외래키가 생성)
    private Team team;

    /**
     * 연관관계 편의 메소드<br/>
     * setTeam() Custom <br/>
     * Member의 team 객체 세팅 뿐만 아니라 Team의 memberList 객체도 세팅한다.
     * @param team
     */
    public void addTeamAndMember(Team team) {
        this.team = team;
        team.getMemberList().add(this);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

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

/*    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }*/

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
//                ", team=" + team +
                '}';
    }
}
