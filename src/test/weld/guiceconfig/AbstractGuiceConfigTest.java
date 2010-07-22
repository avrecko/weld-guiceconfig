package weld.guiceconfig;

import com.google.inject.Module;
import junit.framework.TestCase;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.Before;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 13.7.2010
 * Time: 15:40:48
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGuiceConfigTest extends TestCase{

    protected BeanManager manager;

    Weld weld;

    protected abstract Iterable<? extends Module> getModules();

    @Override
    public void setUp() {
        GuiceConfigTestModule.register(getModules());
        weld = new Weld();
        WeldContainer container = weld.initialize();
        manager = container.getBeanManager();
    }

    @Override
    public void tearDown() {
        weld.shutdown();
    }

    public <T> T getReference(Class<T> clazz, Annotation... bindings) {
        Set<Bean<?>> beans = manager.getBeans(clazz, bindings);
        if (beans.isEmpty()) {
            throw new RuntimeException("No bean found with class: " + clazz + " and bindings " + bindings.toString());
        } else if (beans.size() != 1) {
            StringBuilder bs = new StringBuilder("[");
            for (Annotation a : bindings) {
                bs.append(a.toString() + ",");
            }
            bs.append("]");
            throw new RuntimeException("More than one bean found with class: " + clazz + " and bindings " + bs);
        }
        Bean bean = beans.iterator().next();
        return (T) bean.create(manager.createCreationalContext(bean));
    }

}
