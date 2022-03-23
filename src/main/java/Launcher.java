import controller.LoginController;
import controller.administrator.AdministratorCRUDEmployeeController;
import controller.administrator.AdministratorController;
import controller.administrator.AdministratorReportsController;
import controller.employee.*;
import database.JDBConnectionWrapper;
import javafx.application.Application;
import javafx.stage.Stage;
import model.User;
import model.validator.AccountValidator;
import model.validator.ClientValidator;
import model.validator.UserValidator;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.action.ActionRepository;
import repository.action.ActionRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.account.AccountService;
import service.account.AccountServiceMySQL;
import service.action.ActionService;
import service.action.ActionServiceMySQL;
import service.client.ClientService;
import service.client.ClientServiceMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceMySQL;
import view.LoginView;
import view.administrator.AdministratorCRUDEmployeeView;
import view.administrator.AdministratorReportsView;
import view.administrator.AdministratorView;
import view.employee.*;

import java.sql.Connection;

import static database.Constants.Schemas.PRODUCTION;

public class Launcher extends Application {
    final LoginView loginView;
    final EmployeeView employeeView;
    final EmployeeCRUDClientView employeeCRUDClientView;
    final EmployeeCRUDAccountView employeeCRUDAccountView;
    final EmployeeTransferMoneyView employeeTransferMoneyView;
    final EmployeeBillsView employeeBillsView;
    final AdministratorView administratorView;
    final AdministratorCRUDEmployeeView administratorCRUDEmployeeView;
    final AdministratorReportsView administratorReportsView;

    public Launcher() {
        final Connection connection = new JDBConnectionWrapper(PRODUCTION).getConnection();

        final RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        final UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        final ClientRepository clientRepository = new ClientRepositoryMySQL(connection);
        final AccountRepository accountRepository = new AccountRepositoryMySQL(connection);
        final ActionRepository actionRepository = new ActionRepositoryMySQL(connection);

        final User loggedUser = new User();
        final AuthenticationService authenticationService = new AuthenticationServiceMySQL(userRepository,
                rightsRolesRepository, loggedUser);
        final ClientService clientService = new ClientServiceMySQL(clientRepository);
        final AccountService accountService = new AccountServiceMySQL(accountRepository);
        final ActionService actionService = new ActionServiceMySQL(actionRepository);

        final UserValidator userValidator = new UserValidator(userRepository, authenticationService);
        final ClientValidator clientValidator = new ClientValidator(clientRepository);
        final AccountValidator accountValidator = new AccountValidator(accountRepository);

        loginView = new LoginView();

        employeeView = new EmployeeView();

        employeeCRUDClientView = new EmployeeCRUDClientView();

        employeeCRUDAccountView = new EmployeeCRUDAccountView();

        employeeTransferMoneyView = new EmployeeTransferMoneyView();

        employeeBillsView = new EmployeeBillsView();

        administratorView = new AdministratorView();

        administratorCRUDEmployeeView = new AdministratorCRUDEmployeeView();

        administratorReportsView = new AdministratorReportsView();

        new LoginController(loginView, authenticationService, userValidator, employeeView, administratorView);

        new EmployeeController(loginView, employeeView, employeeCRUDClientView, employeeCRUDAccountView, employeeTransferMoneyView, employeeBillsView);

        new EmployeeCRUDClientController(employeeView, employeeCRUDClientView, clientValidator, clientService, actionService,authenticationService);

        new EmployeeCRUDAccountController(employeeView, employeeCRUDAccountView, accountService, accountValidator, actionService, authenticationService);

        new EmployeeTransferMoneyController(employeeView, employeeTransferMoneyView, accountService, accountValidator,authenticationService,actionService);

        new EmployeeBillsController(employeeView, employeeBillsView, accountService, accountValidator, actionService, authenticationService);

        new AdministratorController(loginView, administratorView, administratorCRUDEmployeeView,administratorReportsView);

        new AdministratorCRUDEmployeeController(administratorView, administratorCRUDEmployeeView, userValidator, authenticationService);

        new AdministratorReportsController(administratorView,administratorReportsView,authenticationService,actionService,userValidator);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Front Desk Bank App");
        primaryStage.setScene(loginView.getScene());
        loginView.setPrimaryStage(primaryStage);
        employeeView.setPrimaryStage(primaryStage);
        employeeCRUDClientView.setPrimaryStage(primaryStage);
        employeeCRUDAccountView.setPrimaryStage(primaryStage);
        employeeTransferMoneyView.setPrimaryStage(primaryStage);
        employeeBillsView.setPrimaryStage(primaryStage);
        administratorView.setPrimaryStage(primaryStage);
        administratorCRUDEmployeeView.setPrimaryStage(primaryStage);
        administratorReportsView.setPrimaryStage(primaryStage);
        primaryStage.show();
    }
}
