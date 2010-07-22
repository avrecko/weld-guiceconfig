package weld.guiceconfig.world;

import javax.inject.Singleton;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 21.7.2010
 * Time: 16:32:22
 * To change this template use File | Settings | File Templates.
 */
@CustomQualifier
public interface Mailer {

    void sendMail(String recipient, String subject, String content);

}
