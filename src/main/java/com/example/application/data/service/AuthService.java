package com.example.application.data.service;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.example.application.data.repositories.UserRepository;
import com.example.application.views.addresult.AddResultView;
import com.example.application.views.admin.AdminView;
import com.example.application.views.currentticket.CurrentticketView;
import com.example.application.views.home.BetNowView;
import com.example.application.views.login.LoginView;
import com.example.application.views.logout.LogoutView;
import com.example.application.views.main.MainView;
import com.example.application.views.mybets.MybetsView;
import com.example.application.views.settings.SettingsView;
import com.example.application.views.upcomingMatchesAdder.UpcomingMatchesAdderView;
import com.example.application.views.wallet.WalletView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService extends CrudService<User, Integer> {

    @Override
    protected JpaRepository<User, Integer> getRepository() {
        return userRepository;
    }

    public double getBalance() {
        User existingUser =  userRepository.findById(currentUserID).orElse(null);
        return existingUser.getBalance();
    }

    public void updateBalance(double balance) {
        User existingUser = userRepository.findById(currentUserID).orElse(null);
        existingUser.setBalance(balance);
        userRepository.save(existingUser);
    }

    public String getUserFullName() {
        return userRepository.findById(currentUserID).orElse(null).getFullName();
    }

    public boolean isUser() {
        return userRepository.findById(currentUserID).orElse(null).getRole().equals(Role.USER);
    }

    public String getUsername() {
        return userRepository.findById(currentUserID).orElse(null).getUsername();
    }

    public String getEmail() {
        return userRepository.findById(currentUserID).orElse(null).getEmail();
    }

    public static class AuthorizedRoute{
        public String route;
        public String name;
        public Class<? extends Component> view;
        public AuthorizedRoute(String route, String name, Class<? extends Component> view) {
            this.route = route;
            this.name = name;
            this.view = view;
        }
    }

    public class AuthException extends Exception {
    }

    public boolean usernameExists(String username) {
        return userRepository.getByUsername(username) != null;
    }

    public boolean emailExists(String email) {
        return userRepository.getByEmail(email) != null;
    }


    @Autowired
    private final UserRepository userRepository;

    public static int currentUserID;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void authenticate(String username, String password) throws AuthException {
        User user = userRepository.getByUsername(username);
        if (user != null && user.checkPassword(password)) {
            currentUserID = user.getId();
            VaadinSession.getCurrent().setAttribute(User.class, user);
            createRoutes(user.getRole());
        } else {
            throw new AuthException();
        }
    }

    public boolean verifyPassword(String password) {
        return userRepository.getById(currentUserID).checkPassword(password);
    }

    public void updateUser(String username, String password, String email) {
        User user = userRepository.getById(currentUserID);
        if(!username.isEmpty()) {
            user.setUsername(username);
            userRepository.save(user);
        }
        if(!password.isEmpty()) {
            userRepository.removeById(currentUserID);
            user = new User(user.getUsername(), password, user.getRole(), user.getEmail(), user.getFullName(), user.getDateOfBirth(), user.getBalance());
            userRepository.save(user);
            user = userRepository.getByUsername(user.getUsername());
            currentUserID = user.getId();
        }
        if(!email.isEmpty()) {
            user.setEmail(email);
            userRepository.save(user);
        }
    }

    public void deleteUser(int id) {
        userRepository.removeById(id);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    private void createRoutes(Role role) {
        getAuthorizedRoutes(role).stream()
                .forEach(route ->
                        RouteConfiguration.forSessionScope().setRoute(
                                route.route, route.view, MainView.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(Role role) {
        ArrayList<AuthorizedRoute> routes = new ArrayList<>();

        if (role.equals(Role.USER)) {
            routes.add(new AuthorizedRoute("home", "Bet Now", BetNowView.class));
            routes.add(new AuthorizedRoute("currentticket", "Current ticket", CurrentticketView.class));
            routes.add(new AuthorizedRoute("mybets", "My bets", MybetsView.class));
            routes.add(new AuthorizedRoute("wallet", "Wallet", WalletView.class));
            routes.add(new AuthorizedRoute("settings", "Settings", SettingsView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));

        } else if (role.equals(Role.ADMIN)) {
            routes.add(new AuthorizedRoute("home", "Bet Now", BetNowView.class));
            routes.add(new AuthorizedRoute("admin", "Admin", AdminView.class));
            routes.add(new AuthorizedRoute("addmatches", "Add upcoming matches", UpcomingMatchesAdderView.class));
            routes.add(new AuthorizedRoute("addresult", "Add result", AddResultView.class));
            routes.add(new AuthorizedRoute("settings", "Settings", SettingsView.class));
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));
        }

        return routes;
    }

}
