package com.example.application.data.service;

import com.example.application.data.entity.Role;
import com.example.application.data.entity.User;
import com.example.application.data.repositories.UserRepository;
import com.example.application.views.addresult.AddResultView;
import com.example.application.views.admin.AdminView;
import com.example.application.views.currentticket.CurrentticketView;
import com.example.application.views.home.BetNowView;
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
            routes.add(new AuthorizedRoute("logout", "Logout", LogoutView.class));
        }

        return routes;
    }

}
