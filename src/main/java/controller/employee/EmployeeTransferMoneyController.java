package controller.employee;

import database.Constants;
import model.Account;
import model.Action;
import model.builder.ActionBuilder;
import model.validator.AccountValidator;
import service.account.AccountService;
import service.action.ActionService;
import service.user.AuthenticationService;
import view.employee.EmployeeTransferMoneyView;
import view.employee.EmployeeView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class EmployeeTransferMoneyController {
    private final EmployeeTransferMoneyView employeeTransferMoneyView;
    private final AccountService accountService;
    private final AccountValidator accountValidator;
    private final EmployeeView employeeView;
    private final ActionService actionService;
    private final AuthenticationService authenticationService;


    public EmployeeTransferMoneyController(EmployeeView employeeView, EmployeeTransferMoneyView employeeTransferMoneyView, AccountService accountService, AccountValidator accountValidator, AuthenticationService authenticationService, ActionService actionService) {
        this.employeeView = employeeView;
        this.employeeTransferMoneyView = employeeTransferMoneyView;
        this.accountService = accountService;
        this.accountValidator = accountValidator;
        this.actionService = actionService;
        this.authenticationService = authenticationService;
        transferMoneyButtonAction();
        backButtonAction();
    }

    private void backButtonAction() {
        employeeTransferMoneyView.getBackBtn().setOnAction(event -> {
            employeeTransferMoneyView.clearFields();
            employeeTransferMoneyView.setScene(employeeView.getScene());
        });
    }

    private void transferMoneyButtonAction() {
        employeeTransferMoneyView.getTransferBtn().setOnAction(event -> {
            if (!checkEmptyInputTransferMoney()) {

                addAction(Constants.Rights.TRANSFER);
                Long fromId = Long.parseLong(employeeTransferMoneyView.getTransferFromId());
                Long toId = Long.parseLong(employeeTransferMoneyView.getTransferToId());
                Double amount = Double.parseDouble(employeeTransferMoneyView.getAmount());

                accountValidator.validateTransfer(fromId, toId, amount);
                final List<String> errors = accountValidator.getErrors();

                if (errors.isEmpty()) {
                    Account fromAccount = accountService.findById(fromId);
                    fromAccount.setAmount(fromAccount.getAmount() - amount);
                    accountService.update(fromAccount);

                    Account toAccount = accountService.findById(toId);
                    toAccount.setAmount(toAccount.getAmount() + amount);
                    accountService.update(toAccount);
                    employeeTransferMoneyView.clearFields();
                } else {
                    for (String error : errors) {
                        System.out.println(error);
                    }
                }
            }
        });
    }

    private boolean checkEmptyInputTransferMoney() {
        return employeeTransferMoneyView.getTransferFromId().equals("") || employeeTransferMoneyView.getTransferToId().equals("") || employeeTransferMoneyView.getAmount().equals("");
    }

    private void addAction(String name) {
        Action action = new ActionBuilder()
                .setName(name)
                .setEmployeeId(authenticationService.getLoggedUser().getId())
                .setDateOfCreation(Date.valueOf(LocalDate.now()))
                .build();
        actionService.save(action);
    }
}
