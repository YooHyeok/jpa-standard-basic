package jpql;


import jpql.domain.Member;

import javax.persistence.*;
import java.util.List;

public class JpqlMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlbasic");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
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

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
