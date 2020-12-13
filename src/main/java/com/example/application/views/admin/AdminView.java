package com.example.application.views.admin;

import com.example.application.data.entity.User;
import com.example.application.data.service.AuthService;
import com.example.application.views.login.LoginView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import java.util.Optional;

@PageTitle("Admin")
@CssImport("./styles/views/admin/admin-view.css")
public class AdminView extends Div {

    private Grid<User> grid;

    private TextField fullName = new TextField("Full name");
    private TextField email = new TextField("Email");
    private TextField username = new TextField("Username");
    private DatePicker dateOfBirth = new DatePicker("Date of birth");

    private Button buttonDeleteUser = new Button("Delete user");
    private Button buttonCreateAdminAccount = new Button("Create admin account");

    private Binder<User> binder;

    private User user = new User();

    AuthService authService;

    public AdminView(@Autowired AuthService authService) {
        setId("admin-view");
        this.authService = authService;
        grid = new Grid<>(User.class);
        grid.setColumns("fullName", "email", "username", "dateOfBirth", "role");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.setDataProvider(new CrudServiceDataProvider<User, Void>(authService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<User> userFromBackend = authService.get(event.getValue().getId());
                // when a row is selected but the data is no longer available, refresh grid
                if (userFromBackend.isPresent()) {
                    populateForm(userFromBackend.get());
                } else {
                    refreshGrid();
                }
            } else {
                clearForm();
            }
        });

        addListeners();

        binder = new Binder<>(User.class);

        binder.bindInstanceFields(this);

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);

        add(splitLayout);
    }

    private void addListeners() {
        buttonDeleteUser.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                User user = grid.asSingleSelect().getValue();
                if(user != null) {
                    authService.deleteUser(user.getId());
                    refreshGrid();
                } else {
                    Notification.show("Please select a user to delete.");
                }
            }
        });

        buttonCreateAdminAccount.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                LoginView.MODE = LoginView.ADMIN_MODE;
                UI.getCurrent().getPage().setLocation("login");
                VaadinSession.getCurrent().getSession().invalidate();
                VaadinSession.getCurrent().close();
            }
        });
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(buttonCreateAdminAccount, buttonDeleteUser, grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(User value) {
        this.user = value;
        binder.readBean(this.user);
    }
}
