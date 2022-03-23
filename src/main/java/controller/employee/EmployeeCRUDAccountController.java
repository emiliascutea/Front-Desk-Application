package controller.employee;

import database.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Account;
import model.Action;
import model.builder.AccountBuilder;
import model.builder.ActionBuilder;
import model.validator.AccountValidator;
import service.account.AccountService;
import service.action.ActionService;
import service.user.AuthenticationService;
import view.employee.EmployeeCRUDAccountView;
import view.employee.EmployeeView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class EmployeeCRUDAccountController {
    private final EmployeeView employeeView;
    private final EmployeeCRUDAccountView employeeCRUDAccountView;
    private final AccountService accountService;
    private final AccountValidator accountValidator;
    private ObservableList<Account> observableListView = FXCollections.observableArrayList();
    private final ActionService actionService;
    private final AuthenticationService authenticationService;

    public EmployeeCRUDAccountController(EmployeeView employeeView, EmployeeCRUDAccountView employeeCRUDAccountView, AccountService accountService, AccountValidator accountValidator, ActionService actionService, AuthenticationService authenticationService) {
        this.employeeView = employeeView;
        this.employeeCRUDAccountView = employeeCRUDAccountView;
        this.accountService = accountService;
        this.accountValidator = accountValidator;
        this.actionService = actionService;
        this.authenticationService=authenticationService;
        addButtonAction();
        updateButtonAction();
        viewButtonAction();
        backButtonAction();
    }

    private void backButtonAction() {
        employeeCRUDAccountView.getBackBtn().setOnAction(event -> {
            employeeCRUDAccountView.setScene(employeeView.getScene());
        });
    }

    private void addButtonAction() {
        employeeCRUDAccountView.getAddBtn().setOnAction(event -> {
            if (!checkEmptyInputAccountAdd()) {

                addAction(Constants.Rights.CREATE_ACCOUNT);

                String type = employeeCRUDAccountView.getType_add();
                Double amount = Double.parseDouble(employeeCRUDAccountView.getAmount_add());
                Date dateOfCreation = Date.valueOf(employeeCRUDAccountView.getDateOfCreation_add());

                accountValidator.validateAdd(type, amount);
                final List<String> errors = accountValidator.getErrors();

                if (errors.isEmpty()) {
                    Account account = new AccountBuilder()
                            .setType(type)
                            .setAmount(amount)
                            .setDateOfCreation(dateOfCreation)
                            .build();
                    accountService.save(account);
                } else {
                    for (String error : errors) {
                        System.out.println(error);
                    }
                }
            }
        });
    }

    private void updateButtonAction(){
        employeeCRUDAccountView.getUpdateBtn().setOnAction(event -> {
            if (!checkEmptyInputAccountUpdate()) {

                addAction(Constants.Rights.UPDATE_ACCOUNT);

                Long id = Long.parseLong(employeeCRUDAccountView.getId_update());
                String type = employeeCRUDAccountView.getType_update();
                Double amount = Double.parseDouble(employeeCRUDAccountView.getAmount_update());

                accountValidator.validateUpdate(type, id);

                final List<String> errors = accountValidator.getErrors();

                if (errors.isEmpty()) {
                    Account account = accountService.findById(id);
                    if (account != null) {
                        account.setType(type);
                        account.setAmount(amount);
                        accountService.update(account);
                    } else {
                        errors.add("Client with card nr doesn't exist");
                        for (String error : errors) {
                            System.out.println(error);
                        }
                    }
                } else {
                    for (String error : errors) {
                        System.out.println(error);
                    }
                }
            }
        });
    }

    private void viewButtonAction(){
        employeeCRUDAccountView.getViewAccountBtn().setOnAction(event -> {
            List<Account> accounts = accountService.findAll();
            observableListView.clear();

            addAction(Constants.Rights.VIEW_ACCOUNT);

            try {
                for (Account account : accounts) {
                    observableListView.add(new AccountBuilder()
                            .setAccountId(account.getAccountId())
                            .setType(account.getType())
                            .setAmount(account.getAmount())
                            .setDateOfCreation(account.getDateOfCreation())
                            .build());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            employeeCRUDAccountView.getTable_id().setCellValueFactory(new PropertyValueFactory<>("accountId"));
            employeeCRUDAccountView.getTable_type().setCellValueFactory(new PropertyValueFactory<>("type"));
            employeeCRUDAccountView.getTable_amount().setCellValueFactory(new PropertyValueFactory<>("amount"));
            employeeCRUDAccountView.getTable_dateOfCreation().setCellValueFactory(new PropertyValueFactory<>("dateOfCreation"));
            employeeCRUDAccountView.getTable().setItems(observableListView);
        });
    }


    private boolean checkEmptyInputAccountAdd() {
        return employeeCRUDAccountView.getType_add().equals("") || employeeCRUDAccountView.getAmount_add().equals("") || employeeCRUDAccountView.getDateOfCreation_add().equals("");
    }

    private boolean checkEmptyInputAccountUpdate() {
        return employeeCRUDAccountView.getId_update().equals("") || employeeCRUDAccountView.getAmount_update().equals("") || employeeCRUDAccountView.getType_update().equals("");
    }

    private void addAction(String name){
        Action action = new ActionBuilder()
                .setName(name)
                .setEmployeeId(authenticationService.getLoggedUser().getId())
                .setDateOfCreation(Date.valueOf(LocalDate.now()))
                .build();
        actionService.save(action);
    }
}
