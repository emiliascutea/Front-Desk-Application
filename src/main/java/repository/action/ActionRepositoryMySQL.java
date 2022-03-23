package repository.action;

import model.Action;
import model.builder.AccountBuilder;
import model.builder.ActionBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ACCOUNT;
import static database.Constants.Tables.ACTION;

public class ActionRepositoryMySQL implements ActionRepository {
    private final Connection connection;

    public ActionRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Action> findAll() {
        List<Action> actions = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            String sql = "Select * from action";
            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()){
                actions.add(getActionsFromResultSet(rs));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return actions;
    }

    @Override
    public boolean save(Action action) {
        try{
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO action values (null, ?, ?, ?)");
            insertStatement.setLong(1, action.getEmployeeId());
            insertStatement.setString(2, action.getName());
            insertStatement.setDate(3, action.getDateOfCreation());
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Action> findByDate(Long employeeId, Date date1, Date date2) {
        List<Action> actions = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();

            String fetchUserSql = "Select * from `" + ACTION + "` where `employeeId`=\'" + employeeId + "\' and dateOfCreation >=\'" + date1 + "\' and dateOfCreation <=\'" + date2 + "\'";
            ResultSet resultSet = statement.executeQuery(fetchUserSql);
            resultSet.next();
            while(resultSet.next()){
                actions.add(getActionsFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return null;
    }

    private Action getActionsFromResultSet(ResultSet rs) throws SQLException {
        return new ActionBuilder()
                .setActionId(rs.getLong("actionId"))
                .setEmployeeId(rs.getLong("employeeId"))
                .setName(rs.getString("name"))
                .setDateOfCreation(rs.getDate("dateOfCreation"))
                .build();
    }
}
