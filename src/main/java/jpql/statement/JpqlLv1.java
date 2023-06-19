package jpql.statement;

import jpql.domain.*;
import jpql.dto.MemberDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class JpqlLv1 {

    /** JPQL 기본함수 & 사용자 정의 함수 (concat, ||, locate, size, index, userFunction)<br/> 사용자 정의 함수는 사용 전 방언에 추가해야한다. - 사용하는 DB 방언을 상속받고, 사용자 정의 함수를 등록한다. */
    public void functions(EntityManager em) {
        em.persist(new Member());
        em.flush();
        em.clear();

        // concat() : 문자열 합 연산
        List<String> concatResult = em.createQuery("select concat('a', 'b') from Member m", String.class).getResultList();
        for (String s : concatResult) {
            System.out.println("s = " + s);
        }
        em.clear();

        // || 연산 : 문자열 합 연산
        List<String> orConcatResult = em.createQuery("select 'a' || 'b' from Member m", String.class).getResultList();
        for (String s : orConcatResult) {
            System.out.println("s = " + s);
        }
        em.clear();

        // locate() : 문자열 위치 검색
        List<Integer> locateResult = em.createQuery("select locate('de', 'abcdef') from Member m", Integer.class).getResultList();
        for (Integer s : locateResult) {
            System.out.println("s = " + s);
        }
        em.clear();

        Team team = new Team();
        team.setName("teamA");
        em.persist(team); //영속화

        Member memberA = new Member();
        memberA.setTeam(team); //캐시에 있는 Team에 MemberC 추가
        em.persist(memberA);
        Member memberB = new Member();
        memberB.setTeam(team); //캐시에 있는 Team에 MemberC 추가
        em.persist(memberB);
        Member memberC = new Member();
        memberC.setTeam(team); //캐시에 있는 Team에 MemberC 추가
        em.persist(memberC);

        em.flush();
        em.clear();

        // size() : 해당 엔티티에 대한 컬렉션 크기를 반환해준다.
        List<Integer> sizeResult = em.createQuery("select size(t.memberList) from Team t", Integer.class).getResultList();
        for (Integer s : sizeResult) {
            System.out.println("s = " + s);
        }
        em.clear();

        // index() : 해당 엔티티의 컬럼 @OrderColumn 컬렉션을 위치값을 쓸때 함께 사용이 가능하다. 왠만해서는 사용하지 않는다. (List의 값타입 컬렉션에서 옵션을 줄때 사용)
        /*List<Object[]> indexResult = em.createQuery("select index(3) from Team t").getResultList();
        for (Object s : indexResult) {
            System.out.println("s = " + s);
        }*/

        // 사용자 정의 함수 : MyH2Dialect클래스에 방언 등록 후 persistence.xml에서 hibernate.dialect를 해당 클래스로 변경(클래스 풀패키지 명기)
        Member member = new Member();
        member.setUsername("teamA");
        em.persist(member);
        Member member2 = new Member();
        member2.setUsername("teamB");
        em.persist(member2);
        Member member3 = new Member();
        member3.setUsername("teamC");
        em.persist(member3);
        em.flush();
        em.clear();

        List<String> functionResult = em.createQuery("select function('group_concat', m.username) from Member m", String.class).getResultList();
        for (String s : functionResult) {
            System.out.println("s = " + s);
        }
        em.clear();

        // JPA Hibernate사용시 함수명으로 입력할 수 있다.
        List<String> functionResult2 = em.createQuery("select group_concat(m.username) from Member m", String.class).getResultList();
        for (String s : functionResult2) {
            System.out.println("s = " + s);
        }
        em.clear();
    }

    /** case문(search/simple), coalesce, nullif (nvl과 isnull은 지원안함) */
    public void conditionStatement(EntityManager em) {
        // searchCase
        Member member = new Member();
        member.setUsername("teamA");
        member.setAge(10);
        member.setType(MemberType.ADMIN);
        em.persist(member);
        em.flush();
        em.clear();
        List<String> simpleCaseResult = em.createQuery("select case when m.age <= 10 then '학생요금' when m.age >= 60 then '경로요금' else '일반요금' end from Member m", String.class).getResultList();
        System.out.println("simpleCaseResult = " + simpleCaseResult);
        em.clear();

        // simpleCase
        Team team = new Team();
        team.setName("teamA");
        em.persist(team);
        em.flush();
        List<String> searchCaseResult = em.createQuery("select case t.name when 'teamA' then '인센티브110%' when 'teamB' then '인센티브120' else '인센티브105' end from Team t", String.class).getResultList();
        System.out.println("searchCaseResult = " + searchCaseResult);
        em.clear();

        // coalesce - 사용자 이름이 없으면 이름 없는 회원을 반환
        Member member2 = new Member();
        member2.setUsername(null);
        em.persist(member2);
        em.flush();
        em.clear();
        List<String> coalesceResult = em.createQuery("select coalesce(m.username, '이름 없는 회원') from Member m", String.class)
                .getResultList();
        for (String s : coalesceResult) {
            System.out.println("coalesceResult = " + s);
        }
        em.clear();

        Member member3 = new Member();
        member3.setUsername("관리자");
        em.persist(member3);
        em.flush();
        em.clear();
        // nullIf - 사용자 이름이 '관리자'면 null을 반환하고 나머지는 본인의 이름을 반환
        List<String> nullIfResult = em.createQuery("select nullif(m.username, '관리자') from Member m", String.class)
                .getResultList();
        for (String s : nullIfResult) {
            System.out.println("nullIfResult = " + s);
        }
        em.clear();
    }

    /** JPQL 타입 표현 - 문자, Boolean, Enum */
    public void typeExpressionOrEtcStatement(EntityManager em) {
        Member member = new Member();
        member.setUsername("teamA");
        member.setAge(10);
        member.setType(MemberType.ADMIN);
        em.persist(member);
        em.flush();
        em.clear();
        List<Object[]> typeExpressionQuery = em.createQuery("select m.username, 'HELLO', true from Member m " +
                        "where m.type = jpql.domain.MemberType.ADMIN") //Enum의 경우 풀패키지명을 포함해야한다.
                .getResultList();
        for (Object[] objects : typeExpressionQuery) {
            System.out.println("objects[0] = " + objects[0]);
            System.out.println("objects[1] = " + objects[1]);
            System.out.println("objects[2] = " + objects[2]);
        }
        em.clear();
        // enum 파라미터 바인딩
        List<Object[]> typeExpressionQuery2 = em.createQuery("select m.username, 'HELLO', true from Member m " +
                        "where m.type = :adminType") //Enum의 경우 풀패키지명을 포함해야한다.
                .setParameter("adminType", member.getType())
                .getResultList();
        for (Object[] objects : typeExpressionQuery) {
            System.out.println("objects[0] = " + objects[0]);
            System.out.println("objects[1] = " + objects[1]);
            System.out.println("objects[2] = " + objects[2]);
        }
        em.clear();

        // IS NOT NULL, EXISTS, IN, AND, OR, NOT, =, >, >=, <, <=, <> BEETWEEN, LIKE 등
        List<Object[]> typeExpressionQuery3 = em.createQuery("select m.username, 'HELLO', true from Member m " +
                        "where m.username is not null")
                .getResultList();
        for (Object[] objects : typeExpressionQuery3) {
            System.out.println("objects[0] = " + objects[0]);
            System.out.println("objects[1] = " + objects[1]);
            System.out.println("objects[2] = " + objects[2]);
        }
        em.clear();

        // 엔티티타입(상속관계에서 사용) - type(i) : Item을 상속받은 엔티티 클래스명 혹은 DiscriminatorValue값 (persistence.xml에 엔티티 지정하지 않아 실행 안됨)
            /*Book book = new Book();
            book.setName("JPA");
            book.setAuthor("김영한");
            em.persist(book);
            em.flush();
            List<Item> InheritanceResult = em.createQuery("select i from Item i where type(i) = Book ", Item.class).getResultList();
            System.out.println("InheritanceResult = " + InheritanceResult);
            em.clear();*/
    }

    /** subQuery 문법 <br/> JQPL과 QueryDSL에서 InlineView는 불가능 <br/> -> 가급적 Join으로 해결 or 쿼리 2번 or nativeQuery
     * <br/> 일반적으로 인라인뷰를 사용하는 이유중 하나는 데이터를 가져온 뒤 내부에서 데이터를 축소하고 외부에서는 데이터타입을 변경하는 경우<br/>
     * 혹은 SQL 에서 view에 대한 로직에 대해서 (view가 원하는 문자를 바꾸거나) 할때가 대부분 -> Application에서 처리해야한다. */
    public void subQueryStatement(EntityManager em) {
        // teamA 소속인 회원
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
        List subQuery1 = em.createQuery("select m from Member m where exists (select t from m.team t where t.name = 'teamA')")
                .getResultList();
        em.clear();
        System.out.println("subQuery1 = " + subQuery1);

        // 전체 상품 각각의 재고보다 주문량이 많은 주문들
        Product product = new Product();
        product.setPrice(1000);
        product.setStockAmount(10);
        product.setName("상폼A");
        em.persist(product);
        Order order = new Order();
        order.setProduct(product);
        order.setAddress(new Address("경기도 땡땡", "땡땡동", "00000"));
        order.setOrderAmount(2000);
        em.persist(order);
        em.flush();
        em.clear();
        List subQuery2 = em.createQuery("select o from Order o where o.orderAmount > all (select p.stockAmount from Product p)")
                .getResultList();
        System.out.println("subQuery2 = " + subQuery2);
        em.clear();

        // 어떤 팀이든 팀에 소속된 회원
        List subQuery3 = em.createQuery("select m from Member m where m.team = any (select t from Team t)")
                .getResultList();
        System.out.println("subQuery3 = " + subQuery3);
        em.clear();

        // 스칼라 SubQuery (ex: avg와 같이 결과값이 double인 경우 타입명기를 Double.class로 지정)
        List<Double> scarlaSubQuery = em.createQuery("select (select avg(m2) from Member m2) from Member m join Team t on m.username = t.name", Double.class)
                .getResultList();
        System.out.println("scarlaSubQuery = " + scarlaSubQuery);
        em.clear();
    }

    /** Join 구문 메소드 */
    public void joinStatement(EntityManager em) {
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
        em.clear();
    }

    /** 페이징 처리 <br/> setFirstResult(offset) : 조회 시작 위치 <br/>(offset생략시 0 - offset 1이면 2번째 로우부터 시작) <br/> setMaxResults(limit) :  총 반환할 row갯수 */
    public void pagingStatement(EntityManager em) {
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
        em.clear();
    }


    /** 프로젝션(SELECT) : SELECT절에 조회할 대상을 지정하는 것 */
    public void projectionStatement(EntityManager em) {
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
        Order order = new Order();
        order.setOrderAmount(0);
        order.setProduct(product);
        order.setAddress(new Address("경기도 땡땡", "땡땡동", "00000"));

        em.persist(order);
        em.flush();
        em.clear();

        List<Address> result4 = em.createQuery("select o.address from Orders o", Address.class)
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
    public void part1BasicStatement(EntityManager em) {
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
        em.clear();
    }
}
