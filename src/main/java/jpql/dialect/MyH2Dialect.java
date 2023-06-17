package jpql.dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/** DBMS 방언에맞게 사용자정의 함수를 추가하기위한 클래스 H2Dialect 클래스를 상속받은후 reqisterFunction() 함수를 통해 방언 등록*/
public class MyH2Dialect extends H2Dialect {

    /** 생성자를 통해 등록한다. 등록법은 H2Dialect 클래스를 참조한다. */
    public MyH2Dialect() {
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}
