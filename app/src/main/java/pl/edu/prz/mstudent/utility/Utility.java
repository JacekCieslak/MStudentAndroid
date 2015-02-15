package pl.edu.prz.mstudent.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    private static Pattern pattern;
    private static Matcher matcher;
    //Email Pattern
    private static final String EMAIL_PATTERN =
            "^[0-9]*@stud.prz.edu.pl$";
            //"^[0-9]{6}$";
    private static final String USERNAME_PATTERN =
            "^[0-9]{6}$";
    private static final String NAME_AND_SURNAME_PATTERN =
            "^[A-Z][a-zęóąśłżźćń]*$";

    private static final String PASSWORD_PATTERN =
            "^[A-Za-z0-9]{5,50}$";

    public static boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public static boolean validateUserName(String userNama) {
        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(userNama);
        return matcher.matches();

    }

    public static boolean validateNameAndSurname(String value) {
        pattern = Pattern.compile(NAME_AND_SURNAME_PATTERN);
        matcher = pattern.matcher(value);
        return matcher.matches();

    }

    public static boolean validatePassowrd(String passowrd) {
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(passowrd);
        return matcher.matches();

    }

    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }
}