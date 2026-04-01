package ict.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserBean implements Serializable {
    private int user_id;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    private Gender gender;
    private int role_id;

    public enum Gender {
        MALE('m'),
        FEMALE('f');

        public final char code;
        private static final Map<Character, Gender> genderMap = new HashMap<>();
        static {
            genderMap.put('m', MALE);
            genderMap.put('f', FEMALE);
        }

        private Gender(char code) {
            this.code = code;
        }

        public static Gender fromChar(char c) {
            return genderMap.get(Character.toLowerCase(c));
        }
    }

    public UserBean() {
    }

    public UserBean(int user_id, String username, String password, String first_name, String last_name, String email,
            Gender gender, int role_id) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.gender = gender;
        this.role_id = role_id;
    }

    public UserBean(String username, String password, String first_name, String last_name, String email,
            Gender gender, int role_id) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.gender = gender;
        this.role_id = role_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
}
