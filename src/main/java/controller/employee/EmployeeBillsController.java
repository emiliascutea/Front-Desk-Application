package controller.employee;

import database.Constants;
import model.Account;
import model.Action;
import model.builder.ActionBuilder;
import model.validator.AccountValidator;
import service.account.AccountService;
import service.action.ActionService;
import service.user.AuthenticationService;
import view.employee.EmployeeBillsView;
import view.employee.EmployeeView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class EmployeeBillsController {
    private final EmployeeBillsView employeeBillsView;
    private final AccountService accountService;
    private final AccountValidator accountValidator;
    private final EmployeeView employeeView;
    private final ActionService actionService;
    private final AuthenticationService authenticationService;

    public EmployeeBillsController(EmployeeView employeeView, EmployeeBillsView employeeBillsView, AccountService accountService, AccountValidator accountValidator, ActionService actionService, AuthenticationService authenticationService) {
        this.employeeBillsView = employeeBillsView;
        this.accountService = accountService;
        this.accountValidator = accountValidator;
        this.actionService = actionService;
        this.authenticationService=authenticationService;
        this.employeeView = employeeView;

        payBillsButtonAction();
        backButtonAction();
    }

    private void backButtonAction() {
        employeeBillsView.getBackBtn().setOnAction(event -> {
            employeeBillsView.setScene(employeeView.getScene());
        });
    }

    private void payBillsButtonAction() {
        employeeBillsView.getPayBillsBtn().setOnAction(event -> {
           addAction();
            if (!checkEmptyInputPayBills()) {
                Long fromId = Long.parseLong(employeeBillsView.getPayFromId());
                Double amount = Double.parseDouble(employeeBillsView.getAmount());

                accountValidator.validatePayBill(fromId, amount);
                final List<String> errors = accountValidator.getErrors();

                if (errors.isEmpty()) {
                    Account fromAccount = accountService.findById(fromId);
                    fromAccount.setAmount(fromAccount.getAmount() - amount);
                    accountService.update(fromAccount);
                } else {
                    for (String error : errors) {
                        System.out.println(error);
                    }
                }
            }
        });
    }

    private boolean checkEmptyInputPayBills() {
        return employeeBillsView.getPayFromId().equals("") || employeeBillsView.getAmount().equals("");
    }

    private void addAction(){
        Action action = new ActionBuilder()
                .setName(Constants.Rights.BILLS)
                .setEmployeeId(authenticationService.getLoggedUser().getId())
                .setDateOfCreation(Date.valueOf(LocalDate.now()))
                .build();
        actionService.save(action);
    }
}
