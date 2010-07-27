package weld.guiceconfig.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.interceptor.InvocationContext;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.ArrayDeque;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 27.7.2010
 * Time: 17:30:23
 * To change this template use File | Settings | File Templates.
 */
public class InterceptorChain {
    private static Object[] parameters;


    public static MethodInvocation chain(ArrayDeque<MethodInterceptor> interceptors, final InvocationContext ctx) {

        final Object[] params = ctx.getParameters();
        if (interceptors.isEmpty()) {
            return new MethodInvocation() {
                @Override
                public Method getMethod() {
                    return ctx.getMethod();
                }

                @Override
                public Object[] getArguments() {
                    return params;
                }

                @Override
                public Object proceed() throws Throwable {
                    try {
                        return ctx.proceed();
                    } finally {
                        ctx.setParameters(params);
                    }


                }

                @Override
                public Object getThis() {
                    return ctx.getTarget();
                }

                @Override
                public AccessibleObject getStaticPart() {
                    throw new RuntimeException("getStaticPart() is not supported in " + ctx.getClass().getName());
                }
            };
        }

        final MethodInterceptor first = interceptors.removeFirst();
        final MethodInvocation invocation = chain(interceptors, ctx);

        return new MethodInvocation() {


            @Override
            public Method getMethod() {
                return ctx.getMethod();
            }

            @Override
            public Object[] getArguments() {
                return params;
            }

            @Override
            public Object proceed() throws Throwable {
                try {
                    return first.invoke(invocation);
                } finally {
                    ctx.setParameters(params);
                }

            }

            @Override
            public Object getThis() {
                return ctx.getTarget();
            }

            @Override
            public AccessibleObject getStaticPart() {
                throw new RuntimeException("getStaticPart() is not supported in " + ctx.getClass().getName());
            }
        };

    }

}
