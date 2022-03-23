package controller.employee;

import database.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Action;
import model.Client;
import model.User;
import model.builder.ActionBuilder;
import model.builder.ClientBuilder;
import model.validator.ClientValidator;
import service.action.ActionService;
import service.client.ClientService;
import service.user.AuthenticationService;
import view.employee.EmployeeCRUDClientView;
import view.employee.EmployeeView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class EmployeeCRUDClientController {
    private final EmployeeView employeeView;
    private final EmployeeCRUDClientView employeeCRUDClientView;
    private final ClientValidator clientValidator;
    private final ClientService clientService;
    private ObservableList<Client> observableListView = FXCollections.observableArrayList();
    private final ActionService actionService;
    private final AuthenticationService authenticationService;

    public EmployeeCRUDClientController(EmployeeView employeeView, EmployeeCRUDClientView employeeCRUDClientView, ClientValidator clientValidator, ClientService clientService, ActionService actionService, AuthenticationService authenticationService) {
        this.employeeView = employeeView;
        this.employeeCRUDClientView = employeeCRUDClientView;
        this.clientValidator = clientValidator;
        this.clientService = clientService;
        this.actionService = actionService;
        this.authenticationService = authenticationService;
        addButtonAction();
        updateButtonAction();
        viewButtonAction();
        backButtonAction();
    }

    private void backButtonAction() {
        employeeCRUDClientView.getBackBtn().setOnAction(event -> {
            employeeCRUDClientView.setScene(employeeView.getScene());
        });
    }


    private void addButtonAction() {
        employeeCRUDClientView.getAddBtn().setOnAction(event -> {
            if (!checkEmptyInputClientAdd()) {

                addAction(Constants.Rights.CREATE_CLIENT);

                String name = employeeCRUDClientView.getNameAdd();
                Long cardNumber = Long.parseLong(employeeCRUDClientView.getCardNumberAdd());
                Long cnp = Long.parseLong(employeeCRUDClientView.getCnpAdd());
                String address = employeeCRUDClientView.getAddressAdd();
                Long accountNumber = Long.parseLong(employeeCRUDClientView.getAccountNumberAdd());

                clientValidator.validateAdd(name, cardNumber, cnp, accountNumber);
                final List<String> errors = clientValidator.getErrors();

                if (errors.isEmpty()) {
                    Client client = new ClientBuilder()
                            .setName(name)
                            .setCardNumber(cardNumber)
                            .setPersonalNumericalCode(cnp)
                            .setAddress(address)
                            .setAccount(accountNumber)
                            .build();
                    clientService.save(client);
                } else {
                    for (String error : errors) {
                        System.out.println(error);
                    }
                }
            }
        });
    }

    private void updateButtonAction() {
        employeeCRUDClientView.getUpdateBtn().setOnAction(event -> {
            if (!checkEmptyInputClientUpdate()) {

                addAction(Constants.Rights.UPDATE_CLIENT);

                String name = employeeCRUDClientView.getNameUpdate();
                Long cardNumber = Long.parseLong(employeeCRUDClientView.getCardNumberUpdate());
                Long cnp = Long.parseLong(employeeCRUDClientView.getCnpUpdate());
                String address = employeeCRUDClientView.getAddressUpdate();
                Long accountNumber = Long.parseLong(employeeCRUDClientView.getAccountNumberUpdate());

                clientValidator.validateUpdate(name, cardNumber, cnp, accountNumber);

                final List<String> errors = clientValidator.getErrors();

                if (errors.isEmpty()) {
                    Client client = clientService.findByCardNumber(cardNumber);
                    if (client != null) {
                        client.setName(name);
                        client.setPersonalNumericalCode(cnp);
                        client.setAddress(address);
                        client.setAccount(accountNumber);
                        clientService.update(client);

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

    private void viewButtonAction() {
        employeeCRUDClientView.getViewClientBtn().setOnAction(event -> {
            addAction(Constants.Rights.VIEW_CLIENT);

            List<Client> clients = clientService.findAll();
            observableListView.clear();
            try {
                for (Client client : clients) {
                    observableListView.add(new ClientBuilder()
                            .setPersonalNumericalCode(client.getPersonalNumericalCode())
                            .setName(client.getName())
                            .setCardNumber(client.getCardNumber())
                            .setAddress(client.getAddress())
                            .setAccount(client.getAccount())
                            .build());

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            employeeCRUDClientView.getTable_name().setCellValueFactory(new PropertyValueFactory<>("name"));
            employeeCRUDClientView.getTable_cardNumber().setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
            employeeCRUDClientView.getTable_cnp().setCellValueFactory(new PropertyValueFactory<>("personalNumericalCode"));
            employeeCRUDClientView.getTable_address().setCellValueFactory(new PropertyValueFactory<>("address"));
            employeeCRUDClientView.getTable_accountNumber().setCellValueFactory(new PropertyValueFactory<>("accountId"));
            employeeCRUDClientView.getTable().setItems(observableListView);
        });
    }

    private boolean checkEmptyInputClientAdd() {
        return employeeCRUDClientView.getNameAdd().equals("") || employeeCRUDClientView.getCardNumberAdd().equals("") || employeeCRUDClientView.getCnpAdd().equals("") || employeeCRUDClientView.getAddressAdd().equals("") || employeeCRUDClientView.getAccountNumberAdd().equals("");
    }

    private boolean checkEmptyInputClientUpdate() {
        return employeeCRUDClientView.getNameUpdate().equals("") || employeeCRUDClientView.getCardNumberUpdate().equals("") || employeeCRUDClientView.getCnpUpdate().equals("") || employeeCRUDClientView.getAddressUpdate().equals("") || employeeCRUDClientView.getAccountNumberUpdate().equals("");
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


