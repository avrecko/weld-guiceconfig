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

import com.google.inject.Module;
import com.google.inject.spi.Elements;
import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weld.guiceconfig.aop.ApplyInterceptorAdvicePhase;
import weld.guiceconfig.internal.CdiBindingOracle;
import weld.guiceconfig.internal.Phase;
import weld.guiceconfig.util.FileDataReader;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Sets up programmatic configuration information and invokes phases that hook in linked bindings and programmatic AOP via CDI.
 *
 * @author Alen Vrecko
 */
public class GuiceConfigExtension implements Extension {

    private static final String MODULES_FILE = "META-INF/guiceconfig/Modules.properties";
    private static final String PACKAGES_FILE = "META-INF/guiceconfig/Packages.properties";

    private static final Logger log = LoggerFactory.getLogger(GuiceConfigExtension.class);
    private static final AnnotationInstanceProvider aip = new AnnotationInstanceProvider();

    private CdiBindingOracle oracle;

    private List<Phase> phases = asList(
            new RedefineDefaultInjectionPointsPhase(),
            new ApplyLinkedBindingsAdvicePhase(),
            new ApplyInterceptorAdvicePhase());

    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager) {
        // read the module class names
        List<Class<? extends Module>> classes = readModuleClassNames();
        log.info("Found " + classes.size() + " Guice modues.");
        if (classes.size() == 0) {
            log.warn("No Guice Modules found. Try adding " + MODULES_FILE + " file which includes the " +
                    "fully qualified names of Guice modules.");
        }

        // instantiate the modules
        LinkedList<Module> modules = new LinkedList<Module>();

        for (Class<? extends Module> clazz : classes) {
            try {
                Constructor<? extends Module> ctor = clazz.getConstructor();
                modules.add(ctor.newInstance());
            } catch (Exception e) {
                // I don't think Weld will report this so we log
                log.warn("Failed to instantiate " + clazz.getName() + " via default constructor.", e);
                throw new RuntimeException(e);
            }
        }

        oracle = CdiBindingOracle.process(Elements.getElements(modules));

        for (Phase phase : phases) {
            phase.setUp(oracle);
        }

    }

    public <T> void processAnotated(@Observes ProcessAnnotatedType<T> event, BeanManager manager) {
        AnnotatedTypeBuilder<T> builder = AnnotatedTypeBuilder.newInstance(event.getAnnotatedType());
        builder.readAnnotationsFromUnderlyingType();

        for (Phase phase : phases) {
            phase.processAnnotated(builder, event, manager, aip, oracle);
        }

        event.setAnnotatedType(builder.create());

    }

    public void processAfterBeanDeployment(@Observes AfterBeanDiscovery event) {
        for (Phase phase : phases) {
            phase.afterProcessing();
        }
    }


    private List<Class<? extends Module>> readModuleClassNames() {
        List<Class<? extends Module>> answer = new ArrayList<Class<? extends Module>>();
        try {
            Enumeration<URL> urls = getClass().getClassLoader().getResources(MODULES_FILE);
            while (urls.hasMoreElements()) {

                URL u = urls.nextElement();
                String data = FileDataReader.readUrl(u);
                String[] modules = data.split("\\s");
                for (String module : modules) {

                    if ("".equals(module.trim())) continue;

                    Class res = null;
                    try {
                        res = getClass().getClassLoader().loadClass(module);
                    }
                    catch (ClassNotFoundException e) {
                        res = Thread.currentThread().getContextClassLoader().loadClass(module);
                    }
                    if (res == null) {
                        throw new RuntimeException("Could not instantiate " + module + " via GuiceConfingExtension's classloader and Context CL.");
                    }
                    answer.add(res);
                }
            }
        }
        catch (Exception e) {
            log.warn("Failed to read module class names from " + MODULES_FILE + " and create a corresponding Class instance for each entry in the file.");
            throw new RuntimeException(e);
        }
        return answer;
    }
}
