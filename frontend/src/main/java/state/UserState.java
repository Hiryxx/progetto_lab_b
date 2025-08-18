package state;

public class UserState {
    public static boolean isLoggedIn = false;
    public static String userId = null;

    public static void login(String userId) {
        isLoggedIn = true;
        UserState.userId = userId;
    }
}
