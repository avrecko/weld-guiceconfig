package weld.guiceconfig.maybeBug;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import weld.guiceconfig.AbstractGuiceConfigTest;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 22.7.2010
 * Time: 13:03:05
 * To change this template use File | Settings | File Templates.
 */
public class MaybeBugTest extends AbstractGuiceConfigTest {
    @Override
    protected Iterable<? extends Module> getModules() {
        return Arrays.asList();
    }


    public void testPossibleBug() {

    }
}
