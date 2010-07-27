package weld.guiceconfig.aop;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.inject.spi.InterceptorBinding;
import org.aopalliance.intercept.MethodInterceptor;
import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import weld.guiceconfig.internal.CdiBindingOracle;
import weld.guiceconfig.processing.Phase;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 27.7.2010
 * Time: 18:09:19
 * To change this template use File | Settings | File Templates.
 */
public class ApplyInterceptorAdvicePhase implements Phase {

    private CdiBindingOracle oracle;
    private ImmutableList<InterceptorBinding> interceptorBindings;
    private ImmutableMultimap.Builder<Method, MethodInterceptor> interceptorMethodBindings = ImmutableMultimap.builder();

    @Override
    public void setUp(CdiBindingOracle oracle) {
        this.oracle = oracle;
        interceptorBindings = oracle.interceptors;

    }

    @Override
    public void afterProcessing() {
        GuiceConfigInterceptor.interceptors = interceptorMethodBindings.build();
    }

    @Override
    public void processAnnotated(AnnotatedTypeBuilder builder, ProcessAnnotatedType event, BeanManager manager, AnnotationInstanceProvider aip, CdiBindingOracle oracle) {
        ImmutableList<InterceptorBinding> interceptorBindings = oracle.interceptors;

        Class<?> javaClass = event.getAnnotatedType().getJavaClass();

        Method[] methods = javaClass.getMethods();

        for (Method method : methods) {
            LinkedList<MethodInterceptor> matchedInterceptors = Lists.newLinkedList();
            for (InterceptorBinding binding : interceptorBindings) {
                if (binding.getClassMatcher().matches(javaClass) && binding.getMethodMatcher().matches(method)) {
                    matchedInterceptors.addAll(binding.getInterceptors());
                }
            }

            if (!matchedInterceptors.isEmpty()) {
                builder.addToMethod(method, aip.get(GuiceConfigIntercepted.class, ImmutableMap.<String, Object>of()));
                interceptorMethodBindings.putAll(method, matchedInterceptors);
            }

        }


    }
}
