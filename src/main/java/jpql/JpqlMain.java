package jpql;


import jpql.domain.*;
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
//            projectionStatement(em); // 프로젝션
//            pagingStatement(em); // 페이징 문법
//            joinStatement(em); // Join 문법

            tx.commit();
        } catch (Exception e) {
            System.out.println(e);
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    /** Join 구문 메소드 */
    private static void joinStatement(EntityManager em) {
        Team team = new Team();
        team.setName("teamA");
        em.persist(team);
        Member member = new Member();
        member.setUsername("teamA");
        member.setAge(10);
        member.setTeam(team);
        em.persist(member);
        em.flush();
        em.clear();
        // innerJoin
        List<Member> innerJoinResult = em.createQuery("select m from Member m inner join m.team t", Member.class)
                .getResultList();
        // leftOuterJoin
        List<Member> leftJoinResult = em.createQuery("select m from Member m left join m.team t", Member.class)
                .getResultList();
        // theta조인(CrossJoin)
        List<Member> thetaJoinResult = em.createQuery("select m from Member m, Team t where m.username = t.name", Member.class)
                .getResultList();
        System.out.println("thetaJoinResult = " + thetaJoinResult);

        // On절(JPA 2.1부터 지원)
        // ex) 회원 id가 일치하는 조건을 기준으로 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조회
        List<Member> leftJoinOnResult = em.createQuery("select m from Member m left join m.team t on t.name = 'teamA'", Member.class)
                .getResultList();
        System.out.println("leftJoinOnResult = " + leftJoinOnResult);

        // 회원의 이름과 팀의 이름이 같은 대상 외부조인 => 연관관계가 없기 때문에 조인대상 엔티티를 member로부터 호출하지 않고 직접 지정해줘야한다.
        List<Member> leftJoinOnResult2 = em.createQuery("select m from Member m left join Team t on m.username = t.name", Member.class)
                .getResultList();
        System.out.println("leftJoinOnResult2 = " + leftJoinOnResult2);
    }

    /** 페이징 처리 <br/> setFirstResult(offset) : 조회 시작 위치 <br/>(offset생략시 0 - offset 1이면 2번째 로우부터 시작) <br/> setMaxResults(limit) :  총 반환할 row갯수 */
    private static void pagingStatement(EntityManager em) {
        for (int i = 0; i < 100; i++){
            Member member = new Member();
            member.setUsername("member"+i);
            member.setAge(i);
            em.persist(member);
        }
        em.flush();
        em.clear();

        List<Member> result = em.createQuery("select m from Member m order by m.age asc ", Member.class)
                .setFirstResult(1) //조회 시작 위치 -> Query의 offest (offset생략시 0 - offset 0이면 1번째 1이면 2번째 로우부터 시작)
                .setMaxResults(10) //limit : 총 반환할 row갯수
                .getResultList();
        System.out.println("result.size = " + result.size());
        for (Member memberResult : result) {
            System.out.println("memberResult = " + memberResult);
        }
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
        em.flush();
        em.clear();

        // 묵시적 JOIN
        List<Team> result2 = em.createQuery("select m.team from Member m", Team.class)
                .getResultList(); //실제 SQL은 Join쿼리가 나간다 하지만 join에대한 예측이 불가능하므로 아래와 같이 작성해야한다.
        em.clear();

        //명시적 JOIN
        List<Team> result3 = em.createQuery("select t from Member m join m.team t ", Team.class)
                .getResultList();
        em.clear();

        //임베디드 타입 프로젝션(Address)
        Product product = new Product();
        product.setName("제발되라");
        em.persist(product);

        Address address = new Address();
        address.setCity("경기도 땡땡");
        address.setStreet("땡땡동");
        address.setZipcode("00000");

        Order order = new Order();
        order.setOrderAmount(0);
        order.setProduct(product);
        order.setAddress(address);

        em.persist(order);
        em.flush();
        em.clear();

        List<Address> result4 = em.createQuery("select o.address from Order o", Address.class)
                .getResultList();
        System.out.println("임베디드 = " + result4);
        em.clear();

        //스칼라 타입 프로젝션(& distinct)
        //1. Object[]타입으로 조회
        List result5 = em.createQuery("select distinct m.username, m.age from Member m")
                .getResultList();
        Object[] castingByResult = (Object[])result5.get(0); //object타입으로 반환되는것을 object배열로 타입캐스팅 해야 한다.
        em.clear();
        List<Object[]> result6 = em.createQuery("select distinct m.username, m.age from Member m")
                .getResultList();
        Object[] castingByResult2 = result6.get(0);
        em.clear();

        //3. DTO활용(패키지명을 포함한 전체 클래스명 입력) - new명령어로 조회(순서와 타입이 일치하는 DTO 생성자 필수)
        List<MemberDTO> result7 = em.createQuery("select distinct new jpql.dto.MemberDTO(m.username, m.age) from Member m")
                .getResultList();
        MemberDTO memberDTO = result7.get(0);
        System.out.println("memberDTO = " + memberDTO.getUsername());
        em.clear();
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
