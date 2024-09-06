import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

 class ElectionPollManagementSystem {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/h1";
    static final String USER = "root";
    static final String PASS = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            

            do {
                System.out.println("\n====== Election Poll Management System ======");
                System.out.println("1. Manage Users");
                System.out.println("2. Manage Candidates");
                System.out.println("3. Voting Phase");
                System.out.println("4. View Results");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        manageUsers(conn, scanner);
                        break;
                    case 2:
                        manageCandidates(conn, scanner);
                        break;
                    case 3:
                        votingPhase(conn, scanner);
                        break;
                    case 4:
                        viewResults(conn);
                        break;
                    case 5:
                        System.out.println("Exiting the system...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 5);

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    private static void manageUsers(Connection conn, Scanner scanner) throws SQLException {
        int choice;
        do {
            System.out.println("\n====== Manage Users ======");
            System.out.println("1. Add User");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. View Users");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addUser(conn, scanner);
                    break;
                case 2:
                    updateUser(conn, scanner);
                    break;
                case 3:
                    deleteUser(conn, scanner);
                    break;
                case 4:
                    viewUsers(conn);
                    break;
                case 5:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private static void manageCandidates(Connection conn, Scanner scanner) throws SQLException {
        int choice;
        do {
            System.out.println("\n====== Manage Candidates ======");
            System.out.println("1. Add Candidate");
            System.out.println("2. Update Candidate");
            System.out.println("3. Delete Candidate");
            System.out.println("4. View Candidates");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addCandidate(conn, scanner);
                    break;
                case 2:
                    updateCandidate(conn, scanner);
                    break;
                case 3:
                    deleteCandidate(conn, scanner);
                    break;
                case 4:
                    viewCandidates(conn);
                    break;
                case 5:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private static void votingPhase(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("\n====== Voting Phase ======");
        System.out.print("Enter Voter ID: ");
        String voterId = scanner.nextLine();

        // Check if voter exists and hasn't voted
        String checkVoterQuery = "SELECT * FROM users WHERE voter_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(checkVoterQuery);
        pstmt.setString(1, voterId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            System.out.println("Voter found: " + rs.getString("name"));
            System.out.println("Select a candidate to vote for:");

            String viewCandidatesQuery = "SELECT * FROM candidates";
            Statement stmt = conn.createStatement();
            ResultSet candidates = stmt.executeQuery(viewCandidatesQuery);
            
            while (candidates.next()) {
                System.out.println(candidates.getInt("id") + ". " + candidates.getString("name") + " (" + candidates.getString("party") + ")");
            }
            
            System.out.print("Enter candidate ID: ");
            int candidateId = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            String voteQuery = "INSERT INTO votes (voter_id, candidate_id) VALUES (?, ?)";
            pstmt = conn.prepareStatement(voteQuery);
            pstmt.setString(1, voterId);
            pstmt.setInt(2, candidateId);
            pstmt.executeUpdate();

            String updateCandidateVotes = "UPDATE candidates SET votecount = votecount + 1 WHERE id = ?";
            pstmt = conn.prepareStatement(updateCandidateVotes);
            pstmt.setInt(1, candidateId);
            pstmt.executeUpdate();

            System.out.println("Vote cast successfully!");
        } else {
            System.out.println("Voter not found or already voted.");
        }
    }

    private static void viewResults(Connection conn) throws SQLException {
        System.out.println("\n====== Election Results ======");
        String resultQuery = "SELECT * FROM candidates ORDER BY votecount DESC";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(resultQuery);

        while (rs.next()) {
            System.out.println("Candidate: " + rs.getString("name") + " | Votes: " + rs.getInt("votecount"));
        }
    }

    private static void addUser(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter mobile: ");
        String mobile = scanner.nextLine();
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();

        String voterId = generateVoterId(name, dob);

        String insertUserQuery = "INSERT INTO users (name, mobile, dob, voter_id) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(insertUserQuery);
        pstmt.setString(1, name);
        pstmt.setString(2, mobile);
        pstmt.setDate(3, Date.valueOf(dob));
        pstmt.setString(4, voterId);
        pstmt.executeUpdate();

        System.out.println("User added successfully with Voter ID: " + voterId);
    }

    private static void updateUser(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Voter ID to update: ");
        String voterId = scanner.nextLine();

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new mobile: ");
        String mobile = scanner.nextLine();
        System.out.print("Enter new date of birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();

        String updateUserQuery = "UPDATE users SET name = ?, mobile = ?, dob = ? WHERE voter_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(updateUserQuery);
        pstmt.setString(1, name);
        pstmt.setString(2, mobile);
        pstmt.setDate(3, Date.valueOf(dob));
        pstmt.setString(4, voterId);
        pstmt.executeUpdate();

        System.out.println("User updated successfully.");
    }

    private static void deleteUser(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Voter ID to delete: ");
        String voterId = scanner.nextLine();

        String deleteUserQuery = "DELETE FROM users WHERE voter_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(deleteUserQuery);
        pstmt.setString(1, voterId);
        pstmt.executeUpdate();

        System.out.println("User deleted successfully.");
    }

    private static void viewUsers(Connection conn) throws SQLException {
        System.out.println("\n====== Registered Users ======");
        String viewUsersQuery = "SELECT * FROM users";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(viewUsersQuery);

        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + " | Name: " + rs.getString("name") + " | Mobile: " + rs.getString("mobile") + " | DOB: " + rs.getDate("dob") + " | Voter ID: " + rs.getString("voter_id"));
        }
    }

    private static void addCandidate(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter candidate name: ");
        String name = scanner.nextLine();
        System.out.print("Enter party: ");
        String party = scanner.nextLine();

        String insertCandidateQuery = "INSERT INTO candidates (name, party) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(insertCandidateQuery);
        pstmt.setString(1, name);
        pstmt.setString(2, party);
        pstmt.executeUpdate();

        System.out.println("Candidate added successfully.");
    }

    private static void updateCandidate(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Candidate ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new party: ");
        String party = scanner.nextLine();

        String updateCandidateQuery = "UPDATE candidates SET name = ?, party = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(updateCandidateQuery);
        pstmt.setString(1, name);
        pstmt.setString(2, party);
        pstmt.setInt(3, id);
        pstmt.executeUpdate();

        System.out.println("Candidate updated successfully.");
    }

    private static void deleteCandidate(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Candidate ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        String deleteCandidateQuery = "DELETE FROM candidates WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(deleteCandidateQuery);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();

        System.out.println("Candidate deleted successfully.");
    }

    private static void viewCandidates(Connection conn) throws SQLException {
        System.out.println("\n====== Registered Candidates ======");
        String viewCandidatesQuery = "SELECT * FROM candidates";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(viewCandidatesQuery);

        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + " | Name: " + rs.getString("name") + " | Party: " + rs.getString("party") + " | Votes: " + rs.getInt("votecount"));
        }
    }

    private static String generateVoterId(String name, String dob) {
        return name.substring(0, 3).toUpperCase() + dob.replace("-", "");
    }
     
private static void resultPhase(Connection conn) throws SQLException {
    System.out.println("\n====== Election Results ======");

    // Query to get vote counts for each candidate
    String getResultsQuery = "SELECT c.id, c.name, c.party, c.votecount FROM candidates c ORDER BY c.votecount DESC";
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(getResultsQuery);

    int maxVotes = 0;
    List<String> winners = new ArrayList<>();

    while (rs.next()) {
        int candidateId = rs.getInt("id");
        String candidateName = rs.getString("name");
        String party = rs.getString("party");
        int voteCount = rs.getInt("votecount");

        // Find the maximum number of votes
        if (voteCount > maxVotes) {
            maxVotes = voteCount;
            winners.clear(); // Clear previous winners
            winners.add(candidateName + " (" + party + ") - Votes: " + voteCount);
        } else if (voteCount == maxVotes) {
            // Add to winners if there is a tie
            winners.add(candidateName + " (" + party + ") - Votes: " + voteCount);
        }
    }

    // Display the results
    if (winners.isEmpty()) {
        System.out.println("No results available.");
    } else {
        System.out.println("Winner(s):");
        for (String winner : winners) {
            System.out.println(winner);
        }
    }
}

}
