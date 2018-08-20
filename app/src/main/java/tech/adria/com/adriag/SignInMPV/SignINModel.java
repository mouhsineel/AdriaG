package tech.adria.com.adriag.SignInMPV;

/**
 * Represents the M part in MVP architecture
 * Simple model that returns random messages
 */
public class SignINModel {
    private String[] messages = {"Connected", "Hi : ", "Disconnected"};

    public String generateText(int status) {
        return messages[status];
    }
}
