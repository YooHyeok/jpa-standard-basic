package jpabook.jpashop;

import hellojpa.Member;
import jpabook.jpashop.domain.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaShopMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        //트랜잭션 시작
        /**
         * 트랜잭션 시작
         * JPA에서는 트랜잭션이라는 처리가 중요하다.
         * 모든 데이터를 변경하는 모든 작업은 JPA에서 꼭 Transaction 안에서 작업을 해야한다. (16~17라인 추가)
         * 실제 DB에 저장하는 트랜잭션 단위, 예를 들어 고객의 행위를 하고 나갈때(상품장바구니추가등) 할때마다
         * DB Connection을 얻어 Query를 날리고 종료되는 한 일괄적인 단위의 활동을 할때 만들어줘야한다.
         */
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        //Try Catch 안에서 트랜잭션의 예외를 처리해야 한다.
        try {

            tx.commit();
        } catch (Exception e) {
            tx.rollback(); // 데이터 저장시 문제 발생하면 트랜잭션을 롤백한다.
        } finally {
            em.close(); // 엔티티매니저 닫기.(내부적으로 DB 커넥션을 물고 동작하므로 사용후에는 꼭 닫아줘야한다)
        }
        emf.close(); //전체 애플리케이션이 종료되면 EntityManagerFactory도 닫아줘야한다.
    }
}
