package controller.administrator;

import view.LoginView;
import view.administrator.AdministratorCRUDEmployeeView;
import view.administrator.AdministratorReportsView;
import view.administrator.AdministratorView;

public class AdministratorController {
    private final LoginView loginView;
    private final AdministratorView administratorView;
    private final AdministratorCRUDEmployeeView administratorCRUDEmployeeView;
    private final AdministratorReportsView administratorReportsView;

    public AdministratorController(LoginView loginView, AdministratorView administratorView, AdministratorCRUDEmployeeView administratorCRUDEmployeeView, AdministratorReportsView administratorReportsView) {
        this.loginView = loginView;
        this.administratorView = administratorView;
        this.administratorCRUDEmployeeView = administratorCRUDEmployeeView;
        this.administratorReportsView = administratorReportsView;
        backButtonAction();
        crudEmployeeAction();
        generateReportAction();
    }

    private void backButtonAction() {
        administratorView.getBackBtn().setOnAction(event -> {
            administratorView.setScene(loginView.getScene());
        });
    }

    private void crudEmployeeAction() {
        administratorView.getCrudEmployee().setOnAction(event -> {
            administratorView.setScene(administratorCRUDEmployeeView.getScene());
        });
    }

    private void generateReportAction() {
        administratorView.getGenerateReports().setOnAction(event -> {
            administratorView.setScene(administratorReportsView.getScene());
        });
    }
}
