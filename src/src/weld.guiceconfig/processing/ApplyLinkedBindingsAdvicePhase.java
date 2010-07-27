package weld.guiceconfig.processing;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Maps;
import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weld.guiceconfig.internal.CdiBindingOracle;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import java.lang.annotation.Annotation;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 21.7.2010
 * Time: 19:50:59
 * To change this template use File | Settings | File Templates.
 */
public class ApplyLinkedBindingsAdvicePhase implements Phase {

    private static final Logger log = LoggerFactory.getLogger(ApplyLinkedBindingsAdvicePhase.class);

    @Override
    public void setUp(CdiBindingOracle oracle) {

    }

    @Override
    public void afterProcessing() {

    }

    @Override
    public void processAnnotated(AnnotatedTypeBuilder builder, ProcessAnnotatedType event, BeanManager manager, AnnotationInstanceProvider aip, CdiBindingOracle oracle) {

        Class javaClass = event.getAnnotatedType().getJavaClass();

        ImmutableCollection<Class<? extends Annotation>> classes = oracle.annotations.get(javaClass);

        for (Class<? extends Annotation> aClass : classes) {
            log.info("Adding to Class " + javaClass + " annotation " + aClass);
            builder.addToClass(aip.get(aClass, Maps.<String, Object>newHashMap()));
        }

        Class<? extends Annotation> aClass = oracle.scopes.get(javaClass);

        if (aClass != null) {
            log.info("Adding to Class " + javaClass + " annotation " + aClass);
            builder.addToClass(aip.get(aClass, Maps.<String, Object>newHashMap()));
        }


    }
}
