package com.example.cli;

import java.net.http.*;
import java.net.URI;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;

public class CliClient {
    private static final String GATEWAY = "http://localhost:8080";

    private static HttpClient client = HttpClient.newHttpClient();
    private static Scanner sc = new Scanner(System.in);

    private static String loggedInUserId = null; // will hold user id (from DB)

    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.println("\n=== Event Management CLI ===");
            System.out.println("1. Host Panel");
            System.out.println("2. Customer Panel");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": hostPanel(); break;
                case "2": customerPanel(); break;
                case "3": System.out.println("Bye!"); return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    // ---------------- Host Panel ----------------
    private static void hostPanel() throws Exception {
        while (true) {
            System.out.println("\n=== Host Panel ===");
            System.out.println("1. Create Event");
            System.out.println("2. Edit Event");
            System.out.println("3. Cancel Event");
            System.out.println("4. View All Events");
            System.out.println("5. Back");
            System.out.print("Choose: ");
            String c = sc.nextLine();
            if (c.equals("5")) break;

            switch (c) {
                case "1":
                    System.out.print("Title: "); String title = sc.nextLine();
                    System.out.print("Description: "); String desc = sc.nextLine();
                    System.out.print("Date (YYYY-MM-DD): "); String date = sc.nextLine();

                    String createBody = String.format(
                            "{\"title\":\"%s\",\"description\":\"%s\",\"date\":\"%s\"}", title, desc, date);

                    HttpRequest createReq = HttpRequest.newBuilder(URI.create(GATEWAY+"/events"))
                            .header("Content-Type","application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(createBody))
                            .build();

                    String resp = client.send(createReq, HttpResponse.BodyHandlers.ofString()).body();
                    printResponse(resp);
                    break;

                case "2":
                    System.out.print("Event ID: "); String eid = sc.nextLine();
                    System.out.print("New Title: "); String nt = sc.nextLine();
                    System.out.print("New Description: "); String nd = sc.nextLine();
                    System.out.print("New Date (YYYY-MM-DD): "); String ndt = sc.nextLine();

                    String editBody = String.format(
                            "{\"title\":\"%s\",\"description\":\"%s\",\"date\":\"%s\"}", nt, nd, ndt);

                    HttpRequest editReq = HttpRequest.newBuilder(URI.create(GATEWAY+"/events/"+eid))
                            .header("Content-Type","application/json")
                            .PUT(HttpRequest.BodyPublishers.ofString(editBody))
                            .build();

                    String editResp = client.send(editReq, HttpResponse.BodyHandlers.ofString()).body();
                    printResponse(editResp);
                    break;

                case "3":
                    System.out.print("Event ID: "); String deid = sc.nextLine();
                    HttpRequest delReq = HttpRequest.newBuilder(URI.create(GATEWAY+"/events/"+deid))
                            .DELETE().build();
                    HttpResponse<String> delResp = client.send(delReq, HttpResponse.BodyHandlers.ofString());
                    if (delResp.statusCode() == 200 || delResp.statusCode() == 204) {
                        System.out.println("Event cancelled successfully!");

                    } else {
                        System.out.println("Failed to cancel event. Status: " + delResp.statusCode());
                        printResponse(delResp.body());
                    }
                    break;

                case "4":
                    HttpRequest listReq = HttpRequest.newBuilder(URI.create(GATEWAY+"/events")).GET().build();
                    String listResp = client.send(listReq, HttpResponse.BodyHandlers.ofString()).body();
                    printResponse(listResp);
                    break;


                default: System.out.println("Invalid choice!");
            }
        }
    }

    // ---------------- Customer Panel ----------------
    private static void customerPanel() throws Exception {
        if (loggedInUserId == null) {
            while (loggedInUserId == null) {
                System.out.println("\n=== Customer Authentication ===");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Back");
                System.out.print("Choose: ");
                String c = sc.nextLine();
                if (c.equals("3")) return;

                switch (c) {
                    case "1": // Register
                        System.out.print("Name: "); String name = sc.nextLine();
                        System.out.print("Email: "); String email = sc.nextLine();
                        System.out.print("Password: "); String pass = sc.nextLine();

                        String regBody = String.format("{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                                name, email, pass);

                        HttpRequest regReq = HttpRequest.newBuilder(URI.create(GATEWAY+"/users/register"))
                                .header("Content-Type","application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(regBody))
                                .build();

                        HttpResponse<String> regResp = client.send(regReq, HttpResponse.BodyHandlers.ofString());
                        printResponse(regResp.body());

                        if (regResp.statusCode() == 200 || regResp.statusCode() == 201) {
                            JSONObject obj = new JSONObject(regResp.body());
                            loggedInUserId = obj.optString("id", null);
                            System.out.println("Registered & logged in as: " + email);
                        }
                        break;

                    case "2": // Login
                        System.out.print("Email: "); String le = sc.nextLine();
                        System.out.print("Password: "); String lp = sc.nextLine();

                        String loginBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", le, lp);

                        HttpRequest loginReq = HttpRequest.newBuilder(URI.create(GATEWAY+"/users/login"))
                                .header("Content-Type","application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(loginBody))
                                .build();

                        HttpResponse<String> loginResp = client.send(loginReq, HttpResponse.BodyHandlers.ofString());

                        if (loginResp.statusCode() == 200) {
                            JSONObject obj = new JSONObject(loginResp.body());
                            loggedInUserId = obj.optString("id", null);
                            System.out.println("Logged in as: " + obj.optString("email"));
                        } else {
                            System.out.println("Login failed: " + loginResp.body());
                        }
                        break;

                    default: System.out.println("Invalid!");
                }
            }
        }

        while (true) {
            System.out.println("\n=== Customer Panel (User ID: "+loggedInUserId+") ===");
            System.out.println("1. Browse Events");
            System.out.println("2. Book Ticket");
            System.out.println("3. Logout");
            System.out.print("Choose: ");
            String c = sc.nextLine();

            switch (c) {
                case "1":
                    HttpRequest list = HttpRequest.newBuilder(URI.create(GATEWAY+"/events")).GET().build();
                    String lresp = client.send(list, HttpResponse.BodyHandlers.ofString()).body();
                    printResponse(lresp);
                    break;

                case "2":
                    System.out.print("EventId: "); String beid = sc.nextLine();
                    HttpRequest b = HttpRequest.newBuilder(URI.create(GATEWAY+"/users/"+loggedInUserId+"/book/"+beid))
                            .POST(HttpRequest.BodyPublishers.noBody()).build();
                    String bookResp = client.send(b, HttpResponse.BodyHandlers.ofString()).body();
                    printResponse(bookResp);
                    break;

                case "3":
                    loggedInUserId = null;
                    System.out.println("Logged out.");
                    return;

                default: System.out.println("Invalid choice!");
            }
        }
    }

    // ---------------- Helper: Pretty Print ----------------
    private static void printResponse(String responseBody) {
        try {
            responseBody = responseBody.trim();
            if (responseBody.startsWith("[")) {
                JSONArray arr = new JSONArray(responseBody);
                System.out.printf("%-5s %-20s %-30s %-15s%n", "ID", "Title", "Description", "Date");
                System.out.println("--------------------------------------------------------------------");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    printEventRow(obj);
                }
            } else if (responseBody.startsWith("{")) {
                JSONObject obj = new JSONObject(responseBody);

                // If it's an event
                if (obj.has("title") && obj.has("description")) {
                    System.out.printf("%-5s %-20s %-30s %-15s%n", "ID", "Title", "Description", "Date");
                    System.out.println("--------------------------------------------------------------------");
                    printEventRow(obj);
                }
                // If it's a user
                else if (obj.has("email") && obj.has("name")) {
                    System.out.println("✅ User Info:");
                    System.out.println("ID: " + obj.opt("id"));
                    System.out.println("Name: " + obj.optString("name"));
                    System.out.println("Email: " + obj.optString("email"));
                }
                else {
                    System.out.println(obj.toString(2));
                }
            } else {
                System.out.println("Response: " + responseBody);
            }
        } catch (Exception e) {
            System.out.println("Raw Response: " + responseBody);
        }
    }

    private static void printEventRow(JSONObject obj) {
        int id = obj.optInt("id", -1);
        String title = obj.optString("title", "");
        String desc = obj.optString("description", "");
        String date = obj.optString("date", "");
        if (date.isEmpty()) date = obj.optString("eventDate", "");
        if (date.isEmpty()) date = obj.optString("scheduledDate", "");
        if (date.isEmpty()) date = obj.optString("createdAt", "");

        System.out.printf("%-5d %-20s %-30s %-15s%n", id, title, desc, date);
    }
}
