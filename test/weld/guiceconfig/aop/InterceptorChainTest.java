/*
 * Copyright (C) 2010 Alen Vrecko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 * Test for {@link weld.guiceconfig.aop.InterceptorChain}.
 *
 * @author Alen Vrecko
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
