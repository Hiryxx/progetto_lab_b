package state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import json.JsonUtil;

public class UserState {
    public static boolean isLoggedIn = false;
    public static String cf = null;
    public static String name = null;
    public static String lastname = null;
    public static String email = null;

    public static void login(String userJson) throws JsonProcessingException {
        isLoggedIn = true;
        JsonNode userNode = JsonUtil.fromString(userJson);

        System.out.println("UserState.login: " + userNode.toString());

        UserState.cf = userNode.get("cf").asText();
        UserState.name = userNode.get("name").asText();
        lastname = userNode.get("lastname").asText();
        email = userNode.get("email").asText();

        System.out.println("UserState.login: cf=" + cf + ", name=" + name + ", lastname=" + lastname + ", email=" + email);
    }


    public static void logout() {
        isLoggedIn = false;
        cf = null;
        name = null;
        lastname = null;
        email = null;
    }
}
