package weld.guiceconfig.world;

import javax.inject.Singleton;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 21.7.2010
 * Time: 16:34:25
 * To change this template use File | Settings | File Templates.
 */
public class SynchronousMailer implements Mailer{

    @Override
    public void sendMail(String recipient, String subject, String content) {}
}
