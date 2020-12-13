package com.example.application.views.settings;

import com.example.application.data.service.AuthService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Settings")
@CssImport(value = "./styles/views/settings/settings-view.css", include = "lumo-badge")
public class SettingsView extends Div {

    //TODO: verify input

    private AuthService authService;

    private Span currentUsername;
    private Span currentEmail;

    private Span titleChangeUsername = new Span("Change username");
    private TextField textFieldNewUsername = new TextField("New username");
    private Span titleChangePassword = new Span("Change password");
    private PasswordField textFieldNewPassword = new PasswordField("New password");
    private PasswordField textFieldConfirmNewPassword = new PasswordField("Confirm new password");
    private Span titleChangeEmail = new Span("Change email address");
    private TextField textFieldNewEmail = new TextField("New email address");

    private PasswordField textFieldCurrentPassword = new PasswordField("Current password");
    private Button buttonSaveChanges = new Button("Save changes");
    private Button buttonDeleteAccount = new Button("Delete account");

    public SettingsView(@Autowired AuthService authService) {
        setId("settings-view");
        this.authService = authService;
        add(createLayout(authService));
        addListeners();
    }

    private Component createLayout(AuthService authService) {
        VerticalLayout v = new VerticalLayout();
        currentUsername = new Span(authService.getUsername());
        currentEmail = new Span(authService.getEmail());
        buttonSaveChanges.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        v.add(currentUsername, currentEmail,
                titleChangeUsername, textFieldNewUsername,
                titleChangePassword, textFieldNewPassword, textFieldConfirmNewPassword,
                titleChangeEmail, textFieldNewEmail,
                textFieldCurrentPassword,
                buttonSaveChanges,
                buttonDeleteAccount);
        return v;
    }

    private void addListeners() {
        buttonSaveChanges.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if (authService.verifyPassword(textFieldCurrentPassword.getValue())) {
                    if (!textFieldNewUsername.getValue().isEmpty() &&
                            !validUsername(textFieldNewUsername.getValue())) {
                        Notification.show("Username already exists.");
                        return;
                    }
                    if (!textFieldNewPassword.getValue().isEmpty() &&
                            !validPassword(textFieldNewPassword.getValue(), textFieldConfirmNewPassword.getValue())) {
                        Notification.show("Password must be 7 characters or longer and must be typed the same.");
                        return;
                    }
                    if (!textFieldNewEmail.getValue().isEmpty() &&
                            !validEmail(textFieldNewEmail.getValue())) {
                        Notification.show("Email already exists.");
                        return;
                    }
                    authService.updateUser(textFieldNewUsername.getValue(),
                            textFieldNewPassword.getValue(),
                            textFieldNewEmail.getValue());
                    UI.getCurrent().getPage().reload();
                } else {
                    Notification.show("Invalid password.");
                }
            }
        });

        buttonDeleteAccount.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if (authService.verifyPassword(textFieldCurrentPassword.getValue())) {
                    authService.deleteUser(AuthService.currentUserID);
                    UI.getCurrent().getPage().setLocation("login");
                    VaadinSession.getCurrent().getSession().invalidate();
                    VaadinSession.getCurrent().close();
                } else {
                    Notification.show("Invalid password.");
                }
            }
        });
    }

    private boolean validUsername(String username) {
        return !authService.usernameExists(username);
    }

    private boolean validPassword(String password, String confirmPassword) {
        return (password.length() >= 7) && password.equals(confirmPassword);
    }

    private boolean validEmail(String email) {
        return !authService.emailExists(email);
    }


}
