package weld.guiceconfig.world;

import javax.enterprise.inject.spi.PassivationCapable;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 21.7.2010
 * Time: 16:34:25
 * To change this template use File | Settings | File Templates.
 */
public class SynchronousMailer implements Mailer, PassivationCapable {

    @Override
    public void sendMail(String recipient, String subject, String content) {
    }

    @Override
    public String getId() {
        return "Test";
    }
}
