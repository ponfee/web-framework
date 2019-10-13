/* __________              _____                                          *\
** \______   \____   _____/ ____\____   ____        Ponfee's code         **
**  |     ___/  _ \ /    \   __\/ __ \_/ __ \       (c) 2017-2019, MIT    **
**  |    |  (  <_> )   |  \  | \  ___/\  ___/       http://www.ponfee.cn  **
**  |____|   \____/|___|  /__|  \___  >\___  >                            **
**                      \/          \/     \/                             **
\*                                                                        */

package cn.ponfee.web.framework;

import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * 
 * 
 * @author Ponfee
 */
public class SPELTest {

    public static void main(String[] args) {
        System.out.println(HibernateValidatorConfiguration.class);
        ExpressionParser parser = new SpelExpressionParser();

        //System.out.println(parser.parseExpression("Hi,everybody").getValue(String.class)); // ERROR
        System.out.println(parser.parseExpression("'Hi,everybody'").getValue(String.class));

        //调用 String 方法
        boolean isEmpty = parser.parseExpression("'Hi,everybody'.contains('Hi')").getValue(Boolean.class);
        System.out.println("isEmpty:" + isEmpty);

        String str = parser.parseExpression("T(org.hibernate.validator.HibernateValidatorConfiguration).FAIL_FAST").getValue(String.class);
        System.out.println(str);

        double num = parser.parseExpression("T(java.lang.Math).PI").getValue(double.class);
        System.out.println(num);
        
        System.out.println(parser.parseExpression("T(java.lang.Math).random()").getValue(double.class));
    }
}
