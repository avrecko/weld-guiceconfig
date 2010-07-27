package weld.guiceconfig.processing;

import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weld.guiceconfig.internal.CdiBindingOracle;
import weld.guiceconfig.internal.HardDefault;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 21.7.2010
 * Time: 19:50:19
 * To change this template use File | Settings | File Templates.
 */
public class RedefineDefaultInjectionPointsPhase<T> implements Phase<T> {

    private static final Logger log = LoggerFactory.getLogger(RedefineDefaultInjectionPointsPhase.class);

    @Override
    public void setUp(CdiBindingOracle oracle) {

    }

    @Override
    public void afterProcessing() {

    }

    @Override
    public void processAnnotated(AnnotatedTypeBuilder builder, ProcessAnnotatedType<T> event, BeanManager manager, AnnotationInstanceProvider aip, CdiBindingOracle oracle) {
        redefineConstructorsInjectionPoints(event.getAnnotatedType().getConstructors(), builder, manager, aip, oracle);
    }

    public void redefineConstructorsInjectionPoints(Set<AnnotatedConstructor<T>> constructorSet, AnnotatedTypeBuilder builder, BeanManager manager, AnnotationInstanceProvider aip, CdiBindingOracle oracle) {


        for (AnnotatedConstructor<T> ctor : constructorSet) {
            if (ctor.isAnnotationPresent(Inject.class)) {
                // process c'tor
                List<AnnotatedParameter<T>> annotatedParameters = ctor.getParameters();

                for (AnnotatedParameter<T> parameter : annotatedParameters) {
                    boolean hasQualifier = false;
                    Set<Annotation> set = parameter.getAnnotations();
                    for (Annotation annotation : set) {
                        if (manager.isQualifier(annotation.annotationType())) {
                            hasQualifier = true;
                        }
                    }


                    if (!hasQualifier) {
                        Type type = parameter.getBaseType();
                        if (oracle.annotations.containsKey(type) && oracle.annotations.get((Class) type).size() == 1) {
                            log.info("Adding @HardDefault to param " + parameter.getPosition() + " of c'tor " + ctor.getJavaMember());
                            builder.addToConstructorParameter(ctor.getJavaMember(), parameter.getPosition(), aip.get(HardDefault.class, new HashMap<String, Object>()));

                        }


                    }
                }


            }
        }
    }
}
