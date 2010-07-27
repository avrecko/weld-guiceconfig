package weld.guiceconfig.aop;

import junit.framework.TestCase;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 27.7.2010
 * Time: 18:01:15
 * To change this template use File | Settings | File Templates.
 */
public class InterceptorChainTest extends TestCase {

    int invocationCount = 0;
    int endOfLineInvoked = 0;

    public void testChain() throws Exception {

        MethodInterceptor int1 = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                methodInvocation.proceed();
                methodInvocation.proceed();
                return methodInvocation.proceed();
            }
        };

        MethodInterceptor int2 = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                invocationCount++;
                return methodInvocation.proceed();
            }
        };

        MethodInvocation invocation = InterceptorChain.chain(new ArrayDeque<MethodInterceptor>(Arrays.asList(int1, int2)), new InvocationContext() {
            @Override
            public Object getTarget() {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object[] getParameters() {
                return new Object[0];
            }

            @Override
            public void setParameters(Object[] objects) {

            }

            @Override
            public Map<String, Object> getContextData() {
                return null;
            }

            @Override
            public Object getTimer() {
                return null;
            }

            @Override
            public Object proceed() throws Exception {
                endOfLineInvoked++;
                return null;
            }
        });

        try {
            invocation.proceed();
        } catch (Throwable throwable) {
            // not expected
            fail();
        }

        assertEquals(3, invocationCount);
        assertEquals(3, endOfLineInvoked);


    }

    public void testChainBreakingInvocation() throws Exception {

        MethodInterceptor int1 = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                methodInvocation.proceed();
                methodInvocation.proceed();
                return methodInvocation.proceed();
            }
        };

        MethodInterceptor int2 = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                invocationCount++;
                return null;
            }
        };

        MethodInvocation invocation = InterceptorChain.chain(new ArrayDeque<MethodInterceptor>(Arrays.asList(int1, int2)), new InvocationContext() {
            @Override
            public Object getTarget() {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object[] getParameters() {
                return new Object[0];
            }

            @Override
            public void setParameters(Object[] objects) {

            }

            @Override
            public Map<String, Object> getContextData() {
                return null;
            }

            @Override
            public Object getTimer() {
                return null;
            }

            @Override
            public Object proceed() throws Exception {
                endOfLineInvoked++;
                return null;
            }
        });

        try {
            invocation.proceed();
        } catch (Throwable throwable) {
            // not expected
            fail();
        }

        assertEquals(3, invocationCount);
        assertEquals(0, endOfLineInvoked);


    }

}
