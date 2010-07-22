package weld.guiceconfig;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 13.7.2010
 * Time: 15:41:17
 * To change this template use File | Settings | File Templates.
 */
public class GuiceConfigTestModule extends AbstractModule {

    private static Iterable<? extends Module> modules;

    @Override
    public void configure() {
        for (Module module : modules) {
            install(module);
        }
    }

    public static void register(Iterable<? extends Module> modules) {
       GuiceConfigTestModule.modules = modules;
    }
}
