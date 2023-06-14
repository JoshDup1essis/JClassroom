package JoshuaDuPlessis;

import JoshuaDuPlessis.Account;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StorageDatabase {

    private Connection conn;
    private Statement stmt;
    private String currentUsername;
    
    public StorageDatabase() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            conn = DriverManager.getConnection("jdbc:ucanaccess://JClassroom.accdb");
            stmt = conn.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

   

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
        
    } 
    public String getCurrentUsername() {
        return currentUsername;
    }
    
public void insertAccount(Account account) {
    try {
        // Insert into tblAccount
        String accountSql = "INSERT INTO tblAccount (AccountName, AccountPassword, Teacher) " +
                "VALUES (?, ?, ?)";

        PreparedStatement accountStmt = conn.prepareStatement(accountSql, Statement.RETURN_GENERATED_KEYS);
        accountStmt.setString(1, account.getUsername());
        accountStmt.setString(2, account.getPassword());
        accountStmt.setBoolean(3, account.isTeacher());
        accountStmt.executeUpdate();

        // Retrieve the AccountID of the newly inserted account
        ResultSet rs = accountStmt.getGeneratedKeys();
        int accountID = -1;
        if (rs.next()) {
            accountID = rs.getInt(1);
        }
        rs.close();
        accountStmt.close();

        if (account.isTeacher()) {
            // Insert into tblTeacher if account is a teacher
            String teacherSql = "INSERT INTO tblTeacher (TeacherID, AccountID, Name, noStudents, Age) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement teacherStmt = conn.prepareStatement(teacherSql);
            teacherStmt.setInt(1, accountID + 3);  // TeacherID = AccountID + 3
            teacherStmt.setInt(2, accountID);
            teacherStmt.setString(3, account.getUsername());
            teacherStmt.setInt(4, 0);  // Initial value for noStudents, it will be calculated later
            teacherStmt.setInt(5, account.getAge());
            teacherStmt.executeUpdate();
            teacherStmt.close();
        } else {
            // Insert into tblStudent if account is a student
            String studentSql = "INSERT INTO tblStudent (AccountID, Name, Age) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement studentStmt = conn.prepareStatement(studentSql);
            studentStmt.setInt(1, accountID);
            studentStmt.setString(2, account.getUsername());
            studentStmt.setInt(3, account.getAge());
            studentStmt.executeUpdate();
            studentStmt.close();
        }
    } catch (SQLException ex) {
        System.out.println("Exception: " + ex.getMessage());
    }
}
 public List<String> getStudentNames() {
    List<String> studentNames = new ArrayList<>();

    try {
        String query = "SELECT Name FROM tblStudent";
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String studentName = rs.getString("Name");
            studentNames.add(studentName);
        }

        rs.close();
    } catch (SQLException ex) {
        System.out.println("Exception: " + ex.getMessage());
    }

    return studentNames;
}

public int getClassIdByName(String className) {
    int classId = -1; // Default value if class is not found

    try {
        // Prepare the SQL statement
        String query = "SELECT ClassID FROM tblClasses WHERE ClassName = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, className);

        // Execute the query
        ResultSet resultSet = statement.executeQuery();

        // Check if the class is found
        if (resultSet.next()) {
            classId = resultSet.getInt("ClassID");
        }

        // Close the resources
        resultSet.close();
        statement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return classId;
}




    public void addToClassList(int classID, List<Integer> studentIDs) {
        try {
            String insertQuery = "INSERT INTO tblClassLists (ClassID, StudentID) VALUES (?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(insertQuery);

            for (int studentID : studentIDs) {
                pstmt.setInt(1, classID);
                pstmt.setInt(2, studentID); // Add 1 to match the student ID in the database
                pstmt.executeUpdate();
            }

            pstmt.close();
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    public List<Integer> getClassIds(String accountUsername) {
    List<Integer> classList = new ArrayList<>();

    try {
        // Prepare the SQL statement
        String query = "SELECT ClassID FROM tblClassLists WHERE StudentID IN " +
                "(SELECT StudentID FROM tblStudent WHERE AccountID IN " +
                "(SELECT AccountID FROM tblAccount WHERE AccountName = ?))";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, accountUsername);

        // Execute the query
        ResultSet resultSet = statement.executeQuery();

        // Iterate over the result set and add the ClassIDs to the list
        while (resultSet.next()) {
            int classID = resultSet.getInt("ClassID");
            classList.add(classID);
        }

        // Close the resources
        resultSet.close();
        statement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return classList;
}
    public String getClassName(int classID) {
    String className = "";

    try {
        // Prepare the SQL statement
        String query = "SELECT ClassName FROM tblClasses WHERE ClassID = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, classID);

        // Execute the query
        ResultSet resultSet = statement.executeQuery();

        // Check if the class is found
        if (resultSet.next()) {
            className = resultSet.getString("ClassName");
        }

        // Close the resources
        resultSet.close();
        statement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return className;
}



    

    public String getTeacherAssignments(String username) {
        try {
            String sql = "SELECT Assignments FROM tblTeacher WHERE AccountID IN " +
                    "(SELECT AccountID FROM tblAccount WHERE AccountName = ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            String assignments = "";
            if (rs.next()) {
                assignments = rs.getString("Assignments");
            }
            rs.close();
            pstmt.close();

            return assignments;
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "";
    }
    public int getStudentIdByUsername(String username) {
        try {
            String sql = "SELECT StudentID FROM tblStudent WHERE AccountID = " +
                    "(SELECT AccountID FROM tblAccount WHERE AccountName = ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            int studentId = -1;
            if (rs.next()) {
                studentId = rs.getInt("StudentID");
            }
            rs.close();
            pstmt.close();

            return studentId;

        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return -1;
    }
public int getTeacherIdByUsername(String username) {
    try {
        String sql = "SELECT TeacherID FROM tblTeacher WHERE AccountID = " +
                "(SELECT AccountID FROM tblAccount WHERE AccountName = ?)";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();
       

        int teacherId = -3;
        if (rs.next()) {
            teacherId = rs.getInt("TeacherID");
        }
        rs.close();
        pstmt.close();
 System.out.println(teacherId);
        return teacherId;
       
    } catch (SQLException ex) {
        System.out.println("Exception: " + ex.getMessage());
    }

    return -1;
}



    public int insertClassroom(String className, String classSubject, int teacherId) {
    try {
        String classroomSql = "INSERT INTO tblClasses (ClassName, ClassSubject, TeacherID) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(classroomSql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, className);
        pstmt.setString(2, classSubject);
        pstmt.setInt(3, teacherId);
        pstmt.executeUpdate();
        
        // Retrieve the ClassID of the newly inserted class
        ResultSet rs = pstmt.getGeneratedKeys();
        int classID = -1;
        if (rs.next()) {
            classID = rs.getInt(1);
        }
        rs.close();
        pstmt.close();
        
        return classID;
    } catch (SQLException ex) {
        System.out.println("Exception: " + ex.getMessage());
    }
    
    return -1;
}



    public void close() {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }
    
    
public void addAssignment(int classID, int teacherID, int studentID,String title, String details, java.util.Date dueDate) {
    try {
        // Format the due date as needed for database insertion
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDueDate = dateFormat.format(dueDate);

        // Create the SQL query
        String query = "INSERT INTO tblAssignments (ClassID, TeacherID, StudentID, AssTitle, Details, DueDate) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, classID);
        pstmt.setInt(2, teacherID);
        pstmt.setInt(3, studentID);
        pstmt.setString(4, title);
        pstmt.setString(5, details);
        pstmt.setString(6, formattedDueDate);

        // Execute the query
        pstmt.executeUpdate();

        // Close the statement
        pstmt.close();
    } catch (SQLException ex) {
        System.out.println("Exception: " + ex.getMessage());
    }
}
public List<Map<String, Object>> getAssignmentsByClassName(String className) {
    List<Map<String, Object>> assignments = new ArrayList<>();

    try {
        // Retrieve the assignments for the given class name
        String query = "SELECT * FROM tblAssignments WHERE ClassID IN (SELECT ClassID FROM tblClasses WHERE ClassName = ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, className);

        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();

        // Iterate over the result set and populate the assignment details
        while (rs.next()) {
            Map<String, Object> assignment = new HashMap<>();
            int numColumns = rsmd.getColumnCount();

            for (int i = 1; i <= numColumns; i++) {
                String columnName = rsmd.getColumnName(i);
                Object columnValue = rs.getObject(i);
                assignment.put(columnName, columnValue);
            }

            assignments.add(assignment);
        }

        rs.close();
        pstmt.close();
    } catch (SQLException ex) {
        System.out.println("Exception: " + ex.getMessage());
    }

    return assignments;
}


    public boolean checkAccountExists(String username, String password) {
        try {
            String sql = "SELECT * FROM tblAccount WHERE AccountName = ? AND AccountPassword = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();

            rs.close();
            pstmt.close();

            return exists;
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return false;
    }
    public int getClassIdByName(String className, int teacherId) {
    try {
        String sql = "SELECT ClassID FROM tblClasses WHERE ClassName = ? AND TeacherID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, className);
        pstmt.setInt(2, teacherId);

        ResultSet rs = pstmt.executeQuery();
        int classId = -1;
        if (rs.next()) {
            classId = rs.getInt("ClassID");
        }
        rs.close();
        pstmt.close();

        return classId;
    } catch (SQLException ex) {
        System.out.println("Exception: " + ex.getMessage());
    }

    return -1;
}
public List<Integer> getStudentIdsByNames(List<String> studentNames) {
    List<Integer> studentIds = new ArrayList<>();

    try {
        String query = "SELECT StudentID FROM tblStudent WHERE Name IN (";
        for (int i = 0; i < studentNames.size(); i++) {
            if (i > 0) {
                query += ",";
            }
            query += "?";
        }
        query += ")";

        PreparedStatement pstmt = conn.prepareStatement(query);
        for (int i = 0; i < studentNames.size(); i++) {
            pstmt.setString(i + 1, studentNames.get(i));
        }

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int studentId = rs.getInt("StudentID");
            studentIds.add(studentId);
        }

        rs.close();
        pstmt.close();
    } catch (SQLException ex) {
        System.out.println("Exception: " + ex.getMessage());
    }

    return studentIds;
}
public List<Integer> getClassIDsByStudentID(int studentID) {
    List<Integer> classIDs = new ArrayList<>();

    try {
        String query = "SELECT ClassID FROM tblClassLists WHERE StudentID = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, studentID);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int classID = rs.getInt("ClassID");
            classIDs.add(classID);
        }

        rs.close();
        pstmt.close();
    } catch (SQLException ex) {
        System.out.println("Exception: " + ex.getMessage());
    }

    return classIDs;
}
public List<String> getTeacherClassNames(int teacherID) {
    List<String> classNames = new ArrayList<>();

    try {
        String query = "SELECT ClassName FROM tblClasses WHERE TeacherID = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, teacherID);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            String className = rs.getString("ClassName");
            classNames.add(className);
        }

        rs.close();
        pstmt.close();
    } catch (SQLException ex) {
        System.out.println("Exception: " + ex.getMessage());
    }

    return classNames;
}

public List<Integer> getClassIDsByTeacherID(int teacherID) {
    List<Integer> classIDs = new ArrayList<>();

    try {
        String query = "SELECT ClassID FROM tblClasses WHERE TeacherID = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, teacherID);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int classID = rs.getInt("ClassID");
            classIDs.add(classID);
        }

        rs.close();
        pstmt.close();
    } catch (SQLException ex) {
        System.out.println("Exception: " + ex.getMessage());
    }

    return classIDs;
}



    public boolean isTeacherAccount(String username) {
        try {
            String sql = "SELECT Teacher FROM tblAccount WHERE AccountName = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            boolean isTeacher = rs.next() && rs.getBoolean("Teacher");

            rs.close();
            pstmt.close();

            return isTeacher;
        } catch (SQLException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return false;
    }

}
