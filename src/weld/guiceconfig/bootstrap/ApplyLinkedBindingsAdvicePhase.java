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

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Maps;
import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weld.guiceconfig.internal.CdiBindingOracle;
import weld.guiceconfig.internal.Phase;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import java.lang.annotation.Annotation;

/**
 * In this phase linked bindings from Guice modules are mapped to CDI on best effort basis.
 *
 * @author Alen Vrecko
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
