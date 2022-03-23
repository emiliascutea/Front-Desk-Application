package controller.administrator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Action;
import model.builder.ActionBuilder;
import model.validator.UserValidator;
import service.action.ActionService;
import service.user.AuthenticationService;
import view.administrator.AdministratorReportsView;
import view.administrator.AdministratorView;

import java.sql.Date;
import java.util.List;

public class AdministratorReportsController {
    private final AdministratorView administratorView;
    private final AdministratorReportsView administratorReportsView;
    private final AuthenticationService authenticationService;
    private final ActionService actionService;
    private final UserValidator userValidator;
    private ObservableList<Action> observableListView = FXCollections.observableArrayList();

    public AdministratorReportsController(AdministratorView administratorView, AdministratorReportsView administratorReportsView, AuthenticationService authenticationService, ActionService actionService, UserValidator userValidator) {
        this.administratorView = administratorView;
        this.administratorReportsView = administratorReportsView;
        this.authenticationService = authenticationService;
        this.actionService = actionService;
        this.userValidator = userValidator;
        backButtonAction();
        generateButtonAction();
    }

    private void backButtonAction() {
        administratorReportsView.getBackBtn().setOnAction(event -> {
            administratorReportsView.setScene(administratorView.getScene());
        });
    }

    private void generateButtonAction() {
        administratorReportsView.getGenerateBtn().setOnAction(event -> {
            if (!checkEmptyInputReports()) {
                Long employeeId = Long.parseLong(administratorReportsView.getEmployeeId());
                Date fromDate = Date.valueOf(administratorReportsView.getFromDate());
                Date toDate = Date.valueOf(administratorReportsView.getToDate());

                userValidator.validateReport(employeeId);
                final List<String> errors = userValidator.getErrors();

                if (errors.isEmpty()) {
                    List<Action> actions = actionService.findByDate(employeeId, fromDate, toDate);
                    observableListView.clear();

                    try {
                        for (Action action : actions) {
                            observableListView.add(new ActionBuilder()
                                    .setName(action.getName())
                                    .setActionId(action.getActionId())
                                    .setEmployeeId(action.getEmployeeId())
                                    .setDateOfCreation(action.getDateOfCreation())
                                    .build());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    administratorReportsView.getTable_employeeId().setCellValueFactory(new PropertyValueFactory<>("employeeId"));
                    administratorReportsView.getTable_name().setCellValueFactory(new PropertyValueFactory<>("name"));
                    administratorReportsView.getTable().setItems(observableListView);
                }

            }

        });
    }

    private boolean checkEmptyInputReports() {
        return administratorReportsView.getEmployeeId().equals("") || administratorReportsView.getFromDate().equals("") || administratorReportsView.getToDate().equals("");
    }
}
