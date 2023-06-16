package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") // Team엔터티와 연결되어있는 Member엔터티의 변수 team을 기준으로 mapping되어있음(team이 연관관계의 주인임을 명시)
//    private List<Member> memberList;
    private List<Member> memberList = new ArrayList<>();

    /**
     * 연관관계 편의 메소드<br/>
     * Member의 team 객체 세팅 뿐만 아니라 Team의 memberList 객체도 세팅한다.
     * @param member
     */
    public void addMemberAndTeam(Member member) {
        memberList.add(member);
        member.setTeam(this);
    }

    public List<Member> getMemberList() {
//        memberList = new ArrayList<>();
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

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

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", memberList=" + memberList +
                '}';
    }
}
