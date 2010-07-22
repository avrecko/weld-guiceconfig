package weld.guiceconfig.internal;

import com.google.inject.BindingAnnotation;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 21.7.2010
 * Time: 18:55:25
 * To change this template use File | Settings | File Templates.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface HardDefault {
}
