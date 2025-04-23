package lk.ijse.gdse71.serenitytherapycenter.util;

import lombok.Getter;
import lombok.Setter;

/**
 * --------------------------------------------
 * Author: Zeenathul Ilma
 * GitHub: https://github.com/Seenathul-Ilma
 * Website: https://zeenathulilma.vercel.app/
 * --------------------------------------------
 * Created: 4/23/2025 7:00 PM
 * Project: SerenityTherapyCenter
 * --------------------------------------------
 **/

public class UserSession {
    private static String loggedRole;

    public static void setRole(String userRole) {
        loggedRole = userRole;
    }

    public static String getRole() {
        return loggedRole;
    }

    public static void clearSession() {
        loggedRole = null;
    }
}
