package com.example.application.views.login;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.example.application.data.service.AuthService;
import com.example.application.views.mybets.MybetsView;
import com.example.application.views.wallet.WalletView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

@Route(value = "login")
@RouteAlias(value = "")
@PageTitle("Betting app")
@CssImport("./styles/views/login/login-view.css")
public class LoginView extends Div {

    public static final String NORMAL_MODE = "normal_mode";
    public static final String ADMIN_MODE = "admin_mode";
    public static String MODE;

    private Role role;

    public LoginView(AuthService authService) {
        setId("login-view");
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        TextField usernameSignup = new TextField("Username");
        PasswordField passwordSignup = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm password");
        TextField email = new TextField("Email address");
        TextField fullName = new TextField("Full name");
        DatePicker dateOfBirth = new DatePicker("Date of birth");

        Span adminMode1 = new Span("");
        Span adminMode2 = new Span("");
        if(MODE != null && MODE.equals(ADMIN_MODE)) {
            adminMode1.setText("ADMIN MODE. SIGN UP WILL CREATE ADMIN ACCOUNT.");
            adminMode2.setText("ADMIN MODE. SIGN UP WILL CREATE ADMIN ACCOUNT.");
            role = Role.ADMIN;
        } else {
            role = Role.USER;
        }

        VerticalLayout login = new VerticalLayout();
        VerticalLayout signup = new VerticalLayout();
        login.add(
                new H1("Welcome to Betting app!"),
                adminMode1,
                username,
                password,
                new Button("Login", event -> {
                    try {
                        System.out.println(username.getValue() + " " + password.getValue());
                        authService.authenticate(username.getValue(), password.getValue());
                        WalletView.currentBalance = authService.getBalance();;
                        MybetsView.bettingTicketId = -1;
                        UI.getCurrent().navigate("home");
                    } catch (AuthService.AuthException e) {
                        Notification.show("Wrong credentials.");
                    }
                }),
                new Button("Sign up", event -> {
                    remove(login);
                    add(signup);
                })
        );

        signup.add(
                new H1("Welcome to Betting app!"),
                adminMode2,
                usernameSignup,
                passwordSignup,
                confirmPassword,
                email,
                fullName,
                dateOfBirth,
                new Button("Create account", event -> {
                    if(inputValid(authService, usernameSignup, passwordSignup, confirmPassword, email, fullName, dateOfBirth)) {
                        authService.createUser(new User(
                                usernameSignup.getValue(),
                                passwordSignup.getValue(),
                                role,
                                email.getValue(),
                                fullName.getValue(),
                                dateOfBirth.getValue()
                        ));
                        try {
                            authService.authenticate(usernameSignup.getValue(), passwordSignup.getValue());
                            WalletView.currentBalance = authService.getBalance();
                            MybetsView.bettingTicketId = -1;
                            UI.getCurrent().navigate("home");
                        } catch (AuthService.AuthException ignored) {}
                    }
                }),
                new Button("I already have an account", event -> {
                    remove(signup);
                    add(login);
                })
        );

        add(login);
    }

    private boolean inputValid(AuthService authService, TextField username, PasswordField password, PasswordField confirmPassword, TextField email, TextField fullName, DatePicker dateOfBirth) {
        if(username.getValue().isEmpty()) {
            Notification.show("Please provide a username.");
            return false;
        }
        if(authService.usernameExists(username.getValue())) {
            Notification.show("Username already exists.");
            return false;
        }
        if(password.getValue().length() < 7) {
            Notification.show("Password must have at least 7 characters.");
            return false;
        }
        if(!password.getValue().equals(confirmPassword.getValue())) {
            Notification.show("Passwords don't match.");
            return false;
        }
        if(email.getValue().isEmpty()) {
            Notification.show("Please provide an email.");
            return false;
        }
        if(authService.emailExists(email.getValue())) {
            Notification.show("Email already used in an account.");
            return false;
        }
        if(fullName.getValue().isEmpty()) {
            Notification.show("Please provide your name.");
            return false;
        }
        if(dateOfBirth.isEmpty()) {
            Notification.show("Please enter your date of birth.");
            return false;
        }
        int age = Period.between(dateOfBirth.getValue(), LocalDate.now()).getYears();
        if(age < 18) {
            Notification.show("You must be 18 years or older to use this app.");
            return false;
        }
        return true;
    }

}
