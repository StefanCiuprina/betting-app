package com.example.application.views.wallet;

import com.example.application.data.service.AuthService;
import com.example.application.views.currentticket.CurrentticketView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Wallet")
@CssImport("styles/views/wallet/wallet-view.css")
public class WalletView extends Div {

    public static double currentBalance;

    private Span titleCurrentAmount = new Span("Current balance: ");
    private Span currentBalanceSpan;
    private TextField amount = new TextField("Amount");

    private Button deposit = new Button("Deposit");
    private Button withdraw = new Button("Withdraw");


    public WalletView(@Autowired AuthService authService){
        setId("wallet-view");
        currentBalanceSpan = new Span(String.valueOf(authService.getBalance()));
        currentBalance = authService.getBalance();

        add(createFormLayout());
        add(createButtonLayout());

        deposit.addClickListener(e -> {
            if(inputValid()) {
                updateCurrentBalance(Double.parseDouble(amount.getValue()));
                authService.updateBalance(currentBalance);
                currentBalanceSpan.setText(String.valueOf(currentBalance));
                Notification.show("Money deposited.");
            }
        });
        withdraw.addClickListener(e -> {
            if(inputValid()) {
                double amount = Double.parseDouble(this.amount.getValue());
                if(amount <= currentBalance) {
                    updateCurrentBalance(-amount);
                    authService.updateBalance(currentBalance);
                    currentBalanceSpan.setText(String.valueOf(currentBalance));
                    Notification.show("Money withdrawn.");
                } else {
                    Notification.show("Insufficient funds!");
                }
            }
        });
    }

//    @Override
//    public void afterNavigation(AfterNavigationEvent event) {
//        authService.updateBalance(currentBalance);
//        currentBalanceSpan.setText(String.valueOf(currentBalance));
//    }

    public static void updateCurrentBalance(double amount) {
        currentBalance += amount;
        currentBalance = CurrentticketView.round(currentBalance, 2);
    }

    private boolean inputValid() {
        try {
            Double.parseDouble(amount.getValue());
        } catch(NumberFormatException e) {
            Notification.show("Invalid input!");
            return false;
        }
        return true;
    }


    private Component createFormLayout() {
        VerticalLayout v = new VerticalLayout();
        HorizontalLayout h = new HorizontalLayout();
        h.add(titleCurrentAmount, currentBalanceSpan);
        FormLayout formLayout = new FormLayout();
        formLayout.add(amount);
        v.add(h, formLayout);
        return v;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        amount.setPreventInvalidInput(true);
        amount.setRequired(true);
        deposit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        withdraw.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(deposit);
        buttonLayout.add(withdraw);
        return buttonLayout;
    }

}
