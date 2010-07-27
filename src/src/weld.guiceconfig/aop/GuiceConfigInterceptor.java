package weld.guiceconfig.aop;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.ArrayDeque;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 27.7.2010
 * Time: 12:10:02
 * To change this template use File | Settings | File Templates.
 */
@Interceptor
@GuiceConfigIntercepted
public class GuiceConfigInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GuiceConfigInterceptor.class);
    static volatile ImmutableMultimap<Method, MethodInterceptor> interceptors;


    @AroundInvoke
    public Object wrap(InvocationContext ctx) throws Throwable {

        Method method = ctx.getMethod();

        ImmutableCollection<MethodInterceptor> configuredInterceptors = interceptors.get(method);

        if (configuredInterceptors == null) {
            log.warn("Failed to find configured interceptors for intercepted method " + method);
            return ctx.proceed();
        }

        MethodInvocation invocation = InterceptorChain.chain(new ArrayDeque<MethodInterceptor>(configuredInterceptors), ctx);
        return invocation.proceed();
    }
}
