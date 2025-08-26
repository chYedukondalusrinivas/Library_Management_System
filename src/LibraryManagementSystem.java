//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


import java.sql.*;
import java.util.*;
import java.util.Date;

// Helper class to store logged-in user info
class LoggedInUser {
    int userId;
    String role;
    String name;


    LoggedInUser(int userId, String role, String name) {
        this.userId = userId;
        this.role = role;
        this.name = name;
    }
}
public class LibraryManagementSystem {
    //----------------------Scanner && Connection object------------------------------------------------
    static Connection con;
    static Scanner sc=new Scanner(System.in);
    static LoggedInUser currentUser; // store logged in user

    //------------------------------------Operations of Admin------------------------------------
    static void addBook() throws SQLException{//---------------------------add book-------------------
        System.out.println("Adding new Book");
        String addSql="Insert into books(title,author,category,total_copies,available_copies) values(?,?,?,?,?)";
        try(PreparedStatement pstmt=con.prepareStatement(addSql);){
            System.out.println("Enter Book title");
            String title=sc.nextLine();
            pstmt.setString(1,title);
            System.out.println("Enter Book author");
            String author=sc.nextLine();
            pstmt.setString(2,author);
            System.out.println("Enter Book category");
            String category=sc.nextLine();
            pstmt.setString(3,category);
            System.out.println("Enter Book total copies");
            int total=sc.nextInt();
            pstmt.setInt(4,total);
            System.out.println("Enter Book Available copies");
            int available=sc.nextInt();
            sc.nextLine();
            pstmt.setInt(5,available);
            pstmt.executeUpdate();
            System.out.println("Book Added Successfully");
        }
    }
    static void deleteBook() throws SQLException{ //-------------delete book----------------------
        System.out.println("Delete a book");
        String deleteSql="delete from books where book_id=?";
        try(PreparedStatement pstmt=con.prepareStatement(deleteSql);){
            System.out.println("Enter book id to delete the book");
            int id=sc.nextInt();
            pstmt.setInt(1,id);
            pstmt.executeUpdate();
            System.out.println("Book Deleted successfully");
        }
    }
    //-------------------------issue book------------------------------
//    static void issueBook(int user_id) throws SQLException{}
    static void viewMembers() throws SQLException {
        String viewSql = "SELECT t.transaction_id, t.user_id, u.user_name, " +
                "t.borrow_date, t.due_date, t.return_date, t.status " +
                "FROM transactions t " +
                "JOIN users u ON t.user_id = u.user_id";

        try (PreparedStatement pstmt = con.prepareStatement(viewSql);
             ResultSet rs = pstmt.executeQuery()) {

            // Define column widths (adjust to your DB schema)
            int w1 = 15, w2 = 10, w3 = 15, w4 = 12, w5 = 12, w6 = 12, w7 = 10;

            // Helper to print separator line
            String sep = "+" + "-".repeat(w1) + "+" + "-".repeat(w2) + "+" + "-".repeat(w3) +
                    "+" + "-".repeat(w4) + "+" + "-".repeat(w5) + "+" + "-".repeat(w6) +
                    "+" + "-".repeat(w7) + "+";

            System.out.println(sep);
            System.out.printf("|%-" + w1 + "s|%-" + w2 + "s|%-" + w3 + "s|%-" + w4 + "s|%-" +
                            w5 + "s|%-" + w6 + "s|%-" + w7 + "s|%n",
                    "transaction_id", "user_id", "user_name",
                    "borrow_date", "due_date", "return_date", "status");
            System.out.println(sep);

            while (rs.next()) {
                int tId = rs.getInt("transaction_id");
                int userId = rs.getInt("user_id");
                String userName = rs.getString("user_name");
                Date borrowDate = rs.getDate("borrow_date");
                Date dueDate = rs.getDate("due_date");
                Date returnDate = rs.getDate("return_date");
                String status = rs.getString("status");

                System.out.printf("|%-" + w1 + "d|%-" + w2 + "d|%-" + w3 + "s|%-" + w4 + "s|%-" +
                                w5 + "s|%-" + w6 + "s|%-" + w7 + "s|%n",
                        tId, userId, userName,
                        borrowDate, dueDate,
                        (returnDate != null ? returnDate.toString() : "NULL"),
                        status);
            }
            System.out.println(sep);
        }
    }

    //    static void collectBooks() throws SQLException{}
    static void deleteMember() throws SQLException{
        String deleteMemberSql="delete from users where user_id=?";
        try(PreparedStatement pstmt= con.prepareStatement(deleteMemberSql)){
            System.out.println("Enter the user id to get deleted");
            int userId=sc.nextInt();
            pstmt.setInt(1,userId);
            pstmt.executeUpdate();
            System.out.println("User deleted Successfully");
        }
    }

    static void viewbooks()throws SQLException{
        String sql="Select * from books";
        try(PreparedStatement pstmt= con.prepareStatement(sql);ResultSet rs=pstmt.executeQuery()){
            int w1=8,w2=40,w3=30,w4=15,w5=13,w6=18;
            String sep = "+" + "-".repeat(w1) + "+" + "-".repeat(w2) + "+" + "-".repeat(w3) +
                    "+" + "-".repeat(w4) + "+" + "-".repeat(w5) + "+" + "-".repeat(w6) +
                    "+";
            System.out.println(sep);
            System.out.printf("|%-" + w1 + "s|%-" + w2 + "s|%-" + w3 + "s|%-" + w4 + "s|%-" +
                            w5 + "s|%-" + w6 + "s|%n",
                    "book_id", "title", "author",
                    "category", "total_copies", "available_copies");
            System.out.println(sep);
            while(rs.next()){
                int id=rs.getInt("book_id");
                String title=rs.getString("title");
                String author=rs.getString("author");
                String category=rs.getString("category");
                int total=rs.getInt("total_copies");
                int available=rs.getInt("available_copies");
                System.out.printf("|%-"+w1+"d|%-"+w2+"s|%-"+w3+"s|%-"+w4+"s|%-"+w5+"d|%-"+w6+"d|%n",id,title,author,category,total,available);
            }
            System.out.println(sep);
        }
    }

    //--------------------------------------Admin dashboard-------------------------------------------
    public static void showAdminDashboard() throws SQLException{
        while(true){
            System.out.println("1.Add book\n2.Delete book\n3.Issue book\n4.View Members\n5.Collect book\n6.Delete member\n7.View books\n8.Logout");
            int choice=sc.nextInt();
            switch(choice){
                case 1:addBook();break;
                case 2:deleteBook();break;
                case 3:
                    //issueBook(45);
                    System.out.println("Currently not available");break;
                case 4:viewMembers();break;
                case 5:
//                    collectBooks();
                    System.out.println("Currently not available");
                    break;
                case 6:deleteMember();break;
                case 7:viewbooks();break;
                case 8:
                    System.out.println("Logging out.....");
                    return;
                default:
                    System.out.println("Enter correct Choice");
            }
        }
    }

    //--------------------------------------Operations of Members---------------------------------------------------
    static void viewAvailableBooks() throws SQLException{
        String sql="Select * from books where available_copies>=1";
        try(PreparedStatement pstmt=con.prepareStatement(sql);
            ResultSet rs=pstmt.executeQuery()){
            int w1=8,w2=40,w3=30,w4=15,w5=13,w6=18;
            String sep = "+" + "-".repeat(w1) + "+" + "-".repeat(w2) + "+" + "-".repeat(w3) +
                    "+" + "-".repeat(w4) + "+" + "-".repeat(w5) + "+" + "-".repeat(w6) +
                    "+";
            System.out.println(sep);
            System.out.printf("|%-" + w1 + "s|%-" + w2 + "s|%-" + w3 + "s|%-" + w4 + "s|%-" +
                            w5 + "s|%-" + w6 + "s|%n",
                    "book_id", "title", "author",
                    "category", "total_copies", "available_copies");
            System.out.println(sep);
            while(rs.next()){
                int id=rs.getInt("book_id");
                String title=rs.getString("title");
                String author=rs.getString("author");
                String category=rs.getString("category");
                int total=rs.getInt("total_copies");
                int available=rs.getInt("available_copies");
                System.out.printf("|%-"+w1+"d|%-"+w2+"s|%-"+w3+"s|%-"+w4+"s|%-"+w5+"d|%-"+w6+"d|%n",id,title,author,category,total,available);
            }
            System.out.println(sep);
        }
    }

    static void borrowBook() throws SQLException{//-------------------------------------borrow book--------------------
        if (currentUser == null) {
            System.out.println("You must be logged in!");
            return;
        }
        System.out.println("Enter book id to borrow:");
        int bookId = sc.nextInt();
        // Step 1: Check if available
        String checkSql = "SELECT available_copies FROM books WHERE book_id=?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkSql)) {
            checkStmt.setInt(1, bookId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    int available = rs.getInt("available_copies");
                    if (available <= 0) {
                        System.out.println("Book not available!");
                        return;
                    }
                } else {
                    System.out.println("Invalid book id!");
                    return;
                }
            }
        }
        // Step 2: Insert transaction
        String insertSql = "INSERT INTO transactions(user_id, book_id, borrow_date, due_date, status) " +
                "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 'borrowed')";
        try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
            pstmt.setInt(1, currentUser.userId);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        }

        // Step 3: Update available copies
        String updateSql = "UPDATE books SET available_copies = available_copies - 1 WHERE book_id=?";
        try (PreparedStatement pstmt = con.prepareStatement(updateSql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        }
        System.out.println("Book borrowed successfully!");
    }
    // -------------------- View Borrowed Books ------------------------
    static void viewBorrowedBooks() throws SQLException {
        if (currentUser == null) {
            System.out.println("Please login first.");
            return;
        }

        String sql = "SELECT t.transaction_id, b.title, b.author, t.borrow_date, t.due_date, t.return_date, t.status " +
                "FROM transactions t " +
                "JOIN books b ON t.book_id = b.book_id " +
                "WHERE t.user_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, currentUser.userId);
            try (ResultSet rs = pstmt.executeQuery()) {

                int w1 = 15, w2 = 30, w3 = 20, w4 = 12, w5 = 12, w6 = 12, w7 = 10;
                String sep = "+" + "-".repeat(w1) + "+" + "-".repeat(w2) + "+" + "-".repeat(w3) +
                        "+" + "-".repeat(w4) + "+" + "-".repeat(w5) + "+" + "-".repeat(w6) +
                        "+" + "-".repeat(w7) + "+";

                System.out.println(sep);
                System.out.printf("|%-" + w1 + "s|%-" + w2 + "s|%-" + w3 + "s|%-" + w4 + "s|%-" +
                                w5 + "s|%-" + w6 + "s|%-" + w7 + "s|%n",
                        "transaction_id", "title", "author",
                        "borrow_date", "due_date", "return_date", "status");
                System.out.println(sep);

                while (rs.next()) {
                    int tId = rs.getInt("transaction_id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    Date borrowDate = rs.getDate("borrow_date");
                    Date dueDate = rs.getDate("due_date");
                    Date returnDate = rs.getDate("return_date");
                    String status = rs.getString("status");

                    System.out.printf("|%-" + w1 + "d|%-" + w2 + "s|%-" + w3 + "s|%-" + w4 + "s|%-" +
                                    w5 + "s|%-" + w6 + "s|%-" + w7 + "s|%n",
                            tId, title, author,
                            borrowDate, dueDate,
                            (returnDate != null ? returnDate.toString() : "NULL"),
                            status);
                }
                System.out.println(sep);
            }
        }
    }

    // -------------------- Return Book ------------------------
    static void returnBook() throws SQLException {
        if (currentUser == null) {
            System.out.println("Please login first.");
            return;
        }

        System.out.println("Enter transaction ID of the book to return:");
        int tId = sc.nextInt();

        String updateSql = "UPDATE transactions SET return_date = ?, status = 'returned' WHERE transaction_id = ? AND user_id = ?";
        String updateBookSql = "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = (SELECT book_id FROM transactions WHERE transaction_id = ?)";

        try (PreparedStatement pstmt = con.prepareStatement(updateSql)) {
            pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
            pstmt.setInt(2, tId);
            pstmt.setInt(3, currentUser.userId);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                try (PreparedStatement pstmt2 = con.prepareStatement(updateBookSql)) {
                    pstmt2.setInt(1, tId);
                    pstmt2.executeUpdate();
                }
                System.out.println("Book returned successfully.");
            } else {
                System.out.println("Invalid transaction ID or you are not authorized.");
            }
        }
    }

    // -------------------- View Due Dates ------------------------
    static void dueDate() throws SQLException {
        if (currentUser == null) {
            System.out.println("Please login first.");
            return;
        }

        String sql = "SELECT t.transaction_id, b.title, t.due_date, t.status " +
                "FROM transactions t " +
                "JOIN books b ON t.book_id = b.book_id " +
                "WHERE t.user_id = ? AND t.status = 'borrowed'";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, currentUser.userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.printf("%-15s %-30s %-15s %-10s%n", "transaction_id", "title", "due_date", "status");
                while (rs.next()) {
                    int tId = rs.getInt("transaction_id");
                    String title = rs.getString("title");
                    Date dueDate = rs.getDate("due_date");
                    String status = rs.getString("status");

                    System.out.printf("%-15d %-30s %-15s %-10s%n", tId, title, dueDate, status);
                }
            }
        }
    }


    //---------------------------------------Members dashboard------------------------------------------------------
    public static void showMemberDashboard() throws SQLException{
        while(true){
            System.out.println("1.View available books\n2.Borrow book\n3.View borrowed books\n4.Return book\n5.See due dates\n6.Logout");
            int choice=sc.nextInt();
            switch (choice){
                case 1:viewAvailableBooks();break;
                case 2:borrowBook();break;
                case 3:viewBorrowedBooks();break;
                case 4:returnBook();break;
                case 5:dueDate();break;
                case 6:
                    System.out.println("Logging out.........");
                    return;
                default:
                    System.out.println("Enter correct choice");
            }
        }
    }

    //---------------------------------Login functionality my-------------------------------------------------------
//    public static String login() throws SQLException{
//        System.out.println("*****_______Login Page_______********");
//        System.out.println("Enter you mail id");
//        String mail=sc.nextLine().trim();
//        System.out.println("Enter your password");
//        String pass=sc.nextLine().trim();
//        String sql="SELECT * FROM users WHERE mail=? AND password=?";
//        try(PreparedStatement pstmt=con.prepareStatement(sql)){
//            pstmt.setString(1,mail);
//            pstmt.setString(2,pass);
//
//            //check if the user exist ot not
//            try(ResultSet rs=pstmt.executeQuery()){
//                if(rs.next()){
//                    System.out.println("Welcome "+rs.getString("user_name"));
//                    System.out.println("Logged in as "+rs.getString("role"));
//                    String role= rs.getString("role");
//                    int id=rs.getInt("user_id");
//                    return  rs.getString("role");
//                }else{
//                    return null;
//                }
//            }
//        }
//    }

    //--------------------------------------Login functionality 2-----------------------------------------------
    public static LoggedInUser login() throws SQLException {
        System.out.println("*****_______Login Page_______********");
        System.out.println("Enter you mail id");
        String mail = sc.next().trim();
        System.out.println("Enter your password");
        String pass = sc.next().trim();


        String sql = "SELECT * FROM users WHERE mail=? AND password=?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, mail);
            pstmt.setString(2, pass);


            //check if the user exist or not
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    String role = rs.getString("role");
                    String name = rs.getString("user_name");
                    System.out.println("Welcome " + name);
                    System.out.println("Logged in as " + role);
                    return new LoggedInUser(id, role, name);
                } else {
                    return null;
                }
            }
        }
    }

    //-------------------------------------to register new members-------------------------------------------------
    public static boolean register() throws SQLException{
        System.out.println("*****_______Register Page_______********");
        System.out.println("Enter you mail id");
        String mail=sc.nextLine().trim();
        System.out.println("Enter your password");
        String pass=sc.nextLine().trim();
        System.out.println("Enter User Name");
        String userName=sc.nextLine().trim();
        String checkUserSql="SELECT * FROM users WHERE mail=?";
        try(PreparedStatement pstmt=con.prepareStatement(checkUserSql)){
            pstmt.setString(1,mail);
            try(ResultSet rs=pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("User already exists! Please login.");
                    return false;
                }
            }
        }
        String sql="INSERT INTO users(user_name,mail,password,role) VALUES(?,?,?,?)";
        try(PreparedStatement pstmt=con.prepareStatement(sql)){
            pstmt.setString(1,userName);
            pstmt.setString(2,mail);
            pstmt.setString(3,pass);
            pstmt.setString(4,"member");
            pstmt.executeUpdate();
            System.out.println("Registration Successful. Now Login");
            return true;
        }
    }

    //---------------------------home page or initial dashboard--------------------------------------------
    public static void homeDashboard() throws SQLException {
        System.out.println("*****_______Home Page_______********");
        while (true) {
            System.out.println("1.Login\n2.Register\n3.Exit");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    currentUser = login();
                    if (currentUser != null) {
                        if (currentUser.role.equals("admin")) {
                            showAdminDashboard();
                        } else if (currentUser.role.equals("member")) {
                            showMemberDashboard();
                        }
                    } else {
                        System.out.println("Invalid credentials");
                    }
                    break;
                case 2:
                    boolean bool = register();
                    if (bool) {
                        currentUser = login();
                        if (currentUser != null && currentUser.role.equals("member")) {
                            showMemberDashboard();
                        }
                    }
                    break;
                case 3:
                    System.out.println("Exiting.....");
                    return;
                default:
                    System.out.println("Select correct choice");
            }
        }
    }
    //-----------------------------------Main function------------------------------------------------------------
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String url="jdbc:mysql://localhost:3306/library_management_system";
        String username="root";
        String password="******";
        Class.forName("com.mysql.cj.jdbc.Driver");
        con=DriverManager.getConnection(url,username,password);
        homeDashboard();
        con.close();
    }
}