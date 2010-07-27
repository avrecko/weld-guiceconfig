package weld.guiceconfig.aop;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 27.7.2010
 * Time: 12:06:12
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@InterceptorBinding
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface GuiceConfigIntercepted {
}
