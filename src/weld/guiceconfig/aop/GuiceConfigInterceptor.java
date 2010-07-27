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
 * Switchboard interceptor that delegates to appropriate interceptors bound and configured in {@link ApplyInterceptorAdvicePhase}  phase.
 *
 * @author Alen Vrecko
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
