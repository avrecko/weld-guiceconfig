package weld.guiceconfig.linkedBasic;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import org.junit.Ignore;
import weld.guiceconfig.AbstractGuiceConfigTest;
import weld.guiceconfig.world.AsynchronousMailer;
import weld.guiceconfig.world.Mailer;

import javax.inject.Singleton;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 20.7.2010
 * Time: 11:32:02
 * To change this template use File | Settings | File Templates.
 */
public class LinkedBindingBasicTest extends AbstractGuiceConfigTest {

    @Override
    protected Iterable<? extends Module> getModules() {
        return Arrays.asList(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Mailer.class).to(AsynchronousMailer.class).in(Singleton.class);
//                bind(AsynchronousMailer.class).in(Singleton.class);
            }
        });
    }

    public void testSimple() {
        LinkedTestUser instance = getReference(LinkedTestUser.class);
        assertEquals(AsynchronousMailer.class, instance.mailer.getClass());

        LinkedTestUser newInstance = getReference(LinkedTestUser.class);
        // we did not say that LinkedTestUser is Singleton
        assertNotSame(instance, newInstance);
        // but we said that Mailer is a Singleton
        assertSame(instance.mailer, newInstance.mailer);
    }

    public void testRacecondition() {
        while (true) {
            LinkedBindingBasicTest test = new LinkedBindingBasicTest();
            test.setUp();
            test.tearDown();
        }
    }

}
