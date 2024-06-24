import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class OnlineExamination {

    private static Map<String, String> users = new HashMap<>();
    private static Map<String, String> profiles = new HashMap<>();
    private static String currentUser = null;
    private static Scanner sc = new Scanner(System.in);
    private static Timer timer;
    private static boolean timeUp = false;

    public static void main(String[] args) {
        users.put("user1", "pass1");
        profiles.put("user1", "User One");

        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Exit");
            int choice = sc.nextInt();
            sc.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            currentUser = username;
            System.out.println("Login successful. Welcome " + profiles.get(username));
            userMenu();
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void userMenu() {
        while (true) {
            System.out.println("1. Update Profile");
            System.out.println("2. Update Password");
            System.out.println("3. Take Exam");
            System.out.println("4. Logout");
            int choice = sc.nextInt();
            sc.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    updateProfile();
                    break;
                case 2:
                    updatePassword();
                    break;
                case 3:
                    takeExam();
                    break;
                case 4:
                    currentUser = null;
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void updateProfile() {
        System.out.print("Enter new profile information: ");
        String newProfile = sc.nextLine();
        profiles.put(currentUser, newProfile);
        System.out.println("Profile updated.");
    }

    private static void updatePassword() {
        System.out.print("Enter new password: ");
        String newPassword = sc.nextLine();
        users.put(currentUser, newPassword);
        System.out.println("Password updated.");
    }

    private static void takeExam() {
        String[] questions = {
            "What is the capital of India?\n1. Gujarat\n2. Goa\n3. New Delhi\n4. Maharasthra",
            "What is 2 + 2?\n1. 3\n2. 4\n3. 5\n4. 6"
        };
        int[] answers = {3, 2}; // correct answers

        AtomicInteger score = new AtomicInteger(0);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                timeUp = true;
                System.out.println("\nTime is up! Exam is auto-submitted.");
                displayScore(score.get(), questions.length);
                userMenu();
            }
        }, 60000); // 1 minute timer

        for (int i = 0; i < questions.length; i++) {
            if (timeUp) break;
            System.out.println(questions[i]);
            System.out.print("Your answer: ");
            int answer = sc.nextInt();
            if (answer == answers[i]) {
                score.getAndIncrement();
            }
        }

        if (!timeUp) {
            timer.cancel();
            displayScore(score.get(), questions.length);
        }
    }

    private static void displayScore(int score, int total) {
        System.out.println("Your score: " + score + "/" + total);
    }
}
