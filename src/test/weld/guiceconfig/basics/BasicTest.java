package weld.guiceconfig.basics;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import weld.guiceconfig.AbstractGuiceConfigTest;
import weld.guiceconfig.world.AsynchronousMailer;
import weld.guiceconfig.world.Mailer;

import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 20.7.2010
 * Time: 11:32:02
 * To change this template use File | Settings | File Templates.
 */
public class BasicTest extends AbstractGuiceConfigTest {

    private static final String AOPED = "AOPED";

    @Override
    protected Iterable<? extends Module> getModules() {
        return Arrays.asList(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Mailer.class).to(AsynchronousMailer.class).in(Singleton.class);

                bindInterceptor(Matchers.any(), new AbstractMatcher<Method>() {
                    @Override
                    public boolean matches(Method o) {
                        return o.getName().equals("test");
                    }
                }, new MethodInterceptor() {
                    @Override
                    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                        methodInvocation.getArguments()[0] = AOPED;
                        methodInvocation.proceed();
                        return AOPED;
                    }
                });

            }
        });
    }

    public void testAop() {
        AopedClass aopedClass = getReference(AopedClass.class);
        String s = aopedClass.test("DUMMY" + AOPED);
        assertEquals(AOPED, s);
        assertEquals(AOPED, aopedClass.getProperty());
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

}
