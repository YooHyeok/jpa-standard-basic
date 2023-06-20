package jpql;


import hellojpa.item.Book;
import jpabook.jpashop.domain.Item;
import jpql.domain.*;
import jpql.dto.MemberDTO;
import jpql.statement.JpqlLv1;
import jpql.statement.JpqlLv2;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JpqlMain {


    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlbasic");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        JpqlLv1 lv1;
        JpqlLv2 lv2;
        tx.begin();
        try {
            lv1 = new JpqlLv1();
            lv2 = new JpqlLv2();
//            lv1.part1BasicStatement(em); // 기본 문법
//            lv1.projectionStatement(em); // 프로젝션
//            lv1.pagingStatement(em); // 페이징 문법
//            lv1.joinStatement(em); // Join 문법
//            lv1.subQueryStatement(em); // subQuery 문법
//            lv1.typeExpressionOrEtcStatement(em); // JPQL 타입 표현 - 문자, Boolean, Enum
//            lv1.conditionStatement(em); // JPQL 조건문 - case(search/simple), coalesce, nullif (nvl과 isnull은 지원안함)
//            lv1.functions(em);

//            lv2.pathExpression(em); // 경로 표현식
//            lv2.fetchJoin(em); // fetch Join
//            lv2.namedQuery(em); // namedQuery

            lv2.bulkOperation(em); //벌크 연산

            tx.commit();
        } catch (Exception e) {
            System.out.println(e);
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }


}
