package repository.action;

import model.Action;

import java.sql.Date;
import java.util.List;

public interface ActionRepository {

    List<Action> findAll();

    boolean save(Action action);

    List<Action> findByDate(Long employeeId, Date date1, Date date2);
}
