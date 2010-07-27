package weld.guiceconfig.processing;

import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weld.guiceconfig.internal.CdiBindingOracle;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 21.7.2010
 * Time: 19:45:57
 * To change this template use File | Settings | File Templates.
 */
public class ClearCdiAnnotationsPhase implements Phase {

    private static final Logger log = LoggerFactory.getLogger(ClearCdiAnnotationsPhase.class);

    @Override
    public void setUp(CdiBindingOracle oracle) {

    }

    @Override
    public void afterProcessing() {

    }

    @Override
    public void processAnnotated(AnnotatedTypeBuilder builder, ProcessAnnotatedType event, BeanManager manager, AnnotationInstanceProvider aip, CdiBindingOracle oracle) {
        AnnotatedType type = event.getAnnotatedType();
        Class javaClass = type.getJavaClass();
        Set<Annotation> annotationSet = type.getAnnotations();
        // remove @Qualifier and @Scope
        for (Annotation annotation : annotationSet) {
            Class<? extends Annotation> clazz = annotation.annotationType();
            if (manager.isQualifier(clazz) || manager.isScope(clazz)) {
                log.info("Removing " + clazz + " from " + javaClass.getName());
                builder.removeFromClass(clazz);
            }
        }
    }
}
