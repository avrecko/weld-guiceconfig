package weld.guiceconfig.world;

import javax.inject.Singleton;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 21.7.2010
 * Time: 16:35:16
 * To change this template use File | Settings | File Templates.
 */
public class AsynchronousMailer implements Mailer{
    @Override
    public void sendMail(String recipient, String subject, String content) {

    }
}
