package jpql;


import jpql.domain.Address;
import jpql.domain.Member;
import jpql.domain.Team;
import jpql.dto.MemberDTO;

import javax.persistence.*;
import java.util.List;

public class JpqlMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlbasic");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            //            part1BasicStatement(em); // 기본 문법

            projectionStatement(em); // 프로젝션

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }


    /** 프로젝션(SELECT) : SELECT절에 조회할 대상을 지정하는 것 */
    private static void projectionStatement(EntityManager em) {
        Member member = new Member();
        member.setUsername("member1");
        member.setAge(10);
        em.persist(member);
        em.flush();
        em.clear();

        //엔티티 프로젝션
        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        Member findMember = result.get(0);
        findMember.setAge(20); //영속성 컨텍스트에서 관리가 되므로 수정이된다.

        // 묵시적 JOIN
        List<Team> result2 = em.createQuery("select m.team from Member m", Team.class)
                .getResultList(); //실제 SQL은 Join쿼리가 나간다 하지만 join에대한 예측이 불가능하므로 아래와 같이 작성해야한다.
        //명시적 JOIN
        List<Team> result3 = em.createQuery("select t from Member m join m.team t ", Team.class)
                .getResultList();
        //임베디드 타입 프로젝션(Address)
        List<Address> result4 = em.createQuery("select o.address from Order o", Address.class)
                .getResultList();
        //스칼라 타입 프로젝션(& distinct)
        //1. Object[]타입으로 조회
        List result5 = em.createQuery("select distinct m.username, m.age from Member m")
                .getResultList();
        Object[] castingByResult = (Object[])result5.get(0); //object타입으로 반환되는것을 object배열로 타입캐스팅 해야 한다.

        List<Object[]> result6 = em.createQuery("select distinct m.username, m.age from Member m")
                .getResultList();
        Object[] castingByResult2 = result6.get(0);

        //3. DTO활용(패키지명을 포함한 전체 클래스명 입력) - new명령어로 조회(순서와 타입이 일치하는 DTO 생성자 필수)
        List<MemberDTO> result7 = em.createQuery("select distinct new jpql.dto.MemberDTO(m.username, m.age) from Member m")
                .getResultList();
        MemberDTO memberDTO = result7.get(0);
        System.out.println("memberDTO = " + memberDTO.getUsername());
    }

    /** 기본 문법 */
    private static void part1BasicStatement(EntityManager em) {
        Member member = new Member();
        member.setUsername("member1");
        member.setAge(10);
        em.persist(member);

        //TypeQuery : 타입정보는 기본적으로 Entity 명기
        TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
        //TypeQuery :  타입정보 명기
        TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
        //Query : 타입정보 X (단일 혹은 복수 지정 컬럼)
        Query query3 = em.createQuery("select b.username, b.age from Member b");

        // getResultList()는 결과가 하나 이상일때 리스트를 반환, 결과가 없으면 빈 리스트를 반환 -> NullPointerException에 대한 문제가 없다.
        List<Member> resultList1 = query1.getResultList();
        List<String> resultList2 = query2.getResultList();
        List<String> resultList3 = query3.getResultList();
        System.out.println("resultList1 = " + resultList1);
        System.out.println("resultList2 = " + resultList2);
        System.out.println("resultList3 = " + resultList3);

        /** 단건 조회 */
        TypedQuery<Member> query4 = em.createQuery("select m from Member m where username = 'member1'", Member.class);
        //getSingleResult()는 결과가 단건, 단일객체 반환 -> 결과가 없으면 NoResultException / 둘 이상이면 NonUniqueResultException 발생
        //SpringDataJpa는 결과가 복수개라면 Optional혹은 Null반환 (Exception이 발생하지 않는다)
        Member singleResult = query4.getSingleResult();
        System.out.println("단건조회 = " + singleResult.getUsername());

        /** 파라미터 바인딩1 - 파라미터명 */
        TypedQuery<Member> query5 = em.createQuery("select m from Member m where username = :username", Member.class);
        query5.setParameter("username", "member1");
        Member singleResult2 = query5.getSingleResult();
        System.out.println("바인딩(이름) = " + singleResult2);

        /** 파라미터 바인딩2 - 파라미터명 */
        TypedQuery<Member> query6 = em.createQuery("select m from Member m where username = ?1", Member.class);
        query6.setParameter(1, "member1");
        Member singleResult3 = query6.getSingleResult();
        System.out.println("바인딩(위치) = " + singleResult3);
        
        /** 메소드 체이닝 */
        Member member2 = em.createQuery("select m from Member m where username = ?1", Member.class)
                .setParameter(1, "member1")
                .getSingleResult();
        System.out.println("메소드 체이닝 결과 = " + member2);


    }
}
