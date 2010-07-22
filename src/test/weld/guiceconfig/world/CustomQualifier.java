package weld.guiceconfig.world;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 21.7.2010
 * Time: 16:36:23
 * To change this template use File | Settings | File Templates.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomQualifier {}
