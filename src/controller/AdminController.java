package controller;

import repository.interfaces.IAdminRepository;
import security.User;
import util.Validator;

import java.util.Scanner;

public class AdminController {
    private final IAdminRepository admin;
    private final Scanner sc;
    private final User currentUser;

    public AdminController(IAdminRepository admin, Scanner sc, User currentUser) {
        this.admin = admin;
        this.sc = sc;
        this.currentUser = currentUser;
    }

    private void requireAdmin() {
        if (currentUser == null || !currentUser.isAdmin()) throw new SecurityException("Access denied: ADMIN only");
    }

    public void createUser() {
        requireAdmin();

        System.out.print("Username: ");
        String u = sc.nextLine();
        Validator.requireNotBlank(u, "Username");

        System.out.print("Password: ");
        String p = sc.nextLine();
        Validator.requireNotBlank(p, "Password");

        boolean ok = admin.createUser(u, p);
        System.out.println(ok ? "Created." : "Failed.");
    }

    public void assignRole() {
        requireAdmin();

        System.out.print("Username: ");
        String u = sc.nextLine();
        Validator.requireNotBlank(u, "Username");

        System.out.print("Role (USER/LIBRARIAN/ADMIN): ");
        String r = sc.nextLine();
        Validator.requireNotBlank(r, "Role");

        boolean ok = admin.assignRole(u, r.trim().toUpperCase());
        System.out.println(ok ? "Assigned." : "Failed.");
    }
}
