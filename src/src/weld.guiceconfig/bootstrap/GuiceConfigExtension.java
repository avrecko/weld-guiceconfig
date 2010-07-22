package weld.guiceconfig.bootstrap;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.inject.Module;
import com.google.inject.spi.Elements;
import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weld.guiceconfig.internal.CdiBindingOracle;
import weld.guiceconfig.processing.ApplyCdiOracleAdvicePhase;
import weld.guiceconfig.processing.Phase;
import weld.guiceconfig.processing.RedefineDefaultInjectionPointsPhase;
import weld.guiceconfig.util.FileDataReader;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 20.7.2010
 * Time: 11:26:50
 * To change this template use File | Settings | File Templates.
 */
public class GuiceConfigExtension implements Extension {

    private static final String MODULES_FILE = "META-INF/guiceconfig/Modules.properties";
    private static final String PACKAGES_FILE = "META-INF/guiceconfig/Packages.properties";

    private static final Logger log = LoggerFactory.getLogger(GuiceConfigExtension.class);
    private static final AnnotationInstanceProvider aip = new AnnotationInstanceProvider();

    private CdiBindingOracle oracle;

    private List<Phase> phases = asList(
            new RedefineDefaultInjectionPointsPhase(),
            new ApplyCdiOracleAdvicePhase());
    private List<String> interestedPackages;

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

        interestedPackages = readGuiceConfigPackages();

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

    }


    private List<String> readGuiceConfigPackages() {
        List<String> answer = Lists.newArrayList();
        try {
            Enumeration<URL> urls = getClass().getClassLoader().getResources(PACKAGES_FILE);
            while (urls.hasMoreElements()) {
                List<String> stringList = Resources.readLines(urls.nextElement(), Charsets.UTF_8);
                answer.addAll(stringList);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return answer;
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
