package weld.guiceconfig.basics;

import weld.guiceconfig.world.Mailer;

import javax.inject.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 21.7.2010
 * Time: 16:37:51
 * To change this template use File | Settings | File Templates.
 */
public class LinkedTestUser {

    public final Mailer mailer;

    @Inject
    public LinkedTestUser(Mailer mailer) {
        this.mailer = mailer;
    }
}
