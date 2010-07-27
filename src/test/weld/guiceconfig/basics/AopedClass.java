package weld.guiceconfig.basics;

/**
 * Created by IntelliJ IDEA.
 * User: avrecko
 * Date: 27.7.2010
 * Time: 11:40:56
 * To change this template use File | Settings | File Templates.
 */
public class AopedClass {

    private String property = "UNASSIGNED";

    public String test(String a) {
        setProperty(a);
        return "DUMMY";
    }


    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
