package weld.guiceconfig.maybeBug;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 22.7.2010
 * Time: 12:38:44
 * To change this template use File | Settings | File Templates.
 */
public class SomeUser implements SomeInterface{


    public SomeUser(@SomeQualifier SomeInterface dependency) {}
}
