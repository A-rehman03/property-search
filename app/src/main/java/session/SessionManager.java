package session;

import android.content.Context;
import android.content.SharedPreferences;

import utils.Constants;

public class SessionManager {

    private static final String PREF_NAME = "realestate_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROLE = "user_role";
    private static final String KEY_LOGGED_IN = "is_logged_in";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_DARK_MODE = "darkMode";


    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createLoginSession(int userId, String role) {
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getUserRole() {
        return prefs.getString(KEY_ROLE, "User");
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
    public void setDarkMode(boolean enabled) {
        editor.putBoolean(Constants.KEY_DARK_MODE, enabled);
        editor.apply();
    }

    public boolean isDarkMode() {
        return prefs.getBoolean(Constants.KEY_DARK_MODE, false);
    }

    public boolean isDarkModeEnabled() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
        }

    public void setDarkModeEnabled(boolean enabled) {
        editor.putBoolean(KEY_DARK_MODE, enabled);
        editor.apply();
    }
}
