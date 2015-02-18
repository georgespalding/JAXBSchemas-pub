import java.util.prefs.Preferences;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 22/09/14
 * Time: 21:02
 * To change this template use File | Settings | File Templates.
 */
public class prefs {
    public static void main(String[] args) {
        System.out.println(System.getProperty("java.util.prefs.userRoot"));
        System.out.println(System.getProperty("user.home"));
        Preferences prefs = Preferences.systemRoot();
        prefs.node("").
    }
}
