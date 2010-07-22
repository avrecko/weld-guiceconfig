package weld.guiceconfig.processing;

import org.jboss.weld.extensions.annotated.AnnotatedTypeBuilder;
import org.jboss.weld.extensions.util.AnnotationInstanceProvider;
import weld.guiceconfig.internal.CdiBindingOracle;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 21.7.2010
 * Time: 19:45:39
 * To change this template use File | Settings | File Templates.
 */
public interface Phase<T> {

    void processAnnotated(AnnotatedTypeBuilder builder, ProcessAnnotatedType<T> event, BeanManager manager,  AnnotationInstanceProvider aip,  CdiBindingOracle oracle);


}
