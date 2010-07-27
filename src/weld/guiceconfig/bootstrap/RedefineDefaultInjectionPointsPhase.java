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

package weld.guiceconfig.bootstrap;

import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weld.guiceconfig.internal.CdiBindingOracle;
import weld.guiceconfig.internal.HardDefault;
import weld.guiceconfig.internal.Phase;

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
 * In order for default linked bindings to work (without annotatedWith) this phase puts {@link weld.guiceconfig.internal.HardDefault} in
 * appropriate places insetad of {@link javax.enterprise.inject.Default}.
 *
 * @author Alen Vrecko
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
