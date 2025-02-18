import java.util.Locale;
import java.util.ResourceBundle;

/**
 * User: Samy Badjoudj
 * Date: 31/01/2015
 */
public class PluginUserMain {
    static public void main(String[] args) {
        Locale currentLocale = Locale.FRANCE;
        ResourceBundle messages;
        messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
        System.out.println(messages.getString("greetings"));
        System.out.println(messages.getString("inquiry"));
        System.out.println(messages.getString("farewell"));
    }
}
