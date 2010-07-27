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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.interceptor.InvocationContext;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.ArrayDeque;

/**
 * Given a set of {@link org.aopalliance.intercept.MethodInterceptor}s and an  {@link InvocationContext} creates a MethodInvocation that will
 * invoke the interceptors in proper order and make sure the proceed works fine.
 *
 * @author Alen Vrecko
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
