package JoshuaDuPlessis;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountValidator {
    
    public static boolean isUsernameValid(String username) {
        return !username.isEmpty() && username.length() >= 5;
    }
    
    public static boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile("[^A-Za-z0-9 ]");
        Matcher matcher = pattern.matcher(password);
        
        return !password.isEmpty() && password.length() >= 6 && matcher.find();
    }
    
    public static boolean isAgeValid(int age) {
        return age >= 13 && age <= 99;
    }
    
    public static boolean isDateValid(Date dateOfBirth, int age) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOfBirth);
        int yearOfBirth = calendar.get(Calendar.YEAR);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return (yearOfBirth + age) == currentYear;
    }
    
    public static boolean isTeacherSelected(boolean teacherSelected) {
        return teacherSelected;
    }
    
    public static boolean validate(String username, String password, Date dateOfBirth, int age, boolean teacherSelected) {
        boolean isValid = true;
        
        if (!isUsernameValid(username)) {
            System.out.println("Invalid username.");
            isValid = false;
        }
        
        if (!isPasswordValid(password)) {
            System.out.println("Invalid password.");
            isValid = false;
        }
        
        if (!isAgeValid(age)) {
            System.out.println("Invalid age.");
            isValid = false;
        }
        
        if (!isDateValid(dateOfBirth, age)) {
            System.out.println("Invalid date of birth.");
            isValid = false;
        }
        
        if (!isTeacherSelected(teacherSelected)) {
            System.out.println("Student Account Created.");
            teacherSelected = false;
        }else {
            System.out.println("Teacher Account Created.");
        }
        
        return isValid;
    }
}
