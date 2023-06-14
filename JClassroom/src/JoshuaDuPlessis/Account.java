package JoshuaDuPlessis;

import java.util.Date;

public class Account {
    private String username;
    private String password;
    private Date YoB;
    private int age;
    private boolean teacher;
    private Student student;
    private int teacherId;
    private int studentId;
    private StorageDatabase storage;

    public Account(String u, String p, Date y, int a, boolean t) {
        username = u;
        password = p;
        YoB = y;
        age = a;
        teacher = t;
        storage = new StorageDatabase();
        if (!t) {
            student = new Student();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String u) {
        username = u;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String p) {
        password = p;
    }

    public Date getYoB() {
        return YoB;
    }

    public void setYoB(Date y) {
        YoB = y;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int a) {
        age = a;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public void setTeacher(boolean t) {
        teacher = t;
        if (!t) {
            student = new Student();
        }
    }

    public Student getStudent() {
        return student;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int id) {
        teacherId = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int id) {
        studentId = id;
    }

    public String getAssignments() {
        if (teacher) {
            return storage.getTeacherAssignments(username);
        }
        return "N/A";
    }
    
    
}
