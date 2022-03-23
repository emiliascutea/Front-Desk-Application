package service.action;

import model.Action;
import repository.action.ActionRepository;

import java.sql.Date;
import java.util.List;

public class ActionServiceMySQL implements ActionService{

    private final ActionRepository actionRepository;

    public ActionServiceMySQL(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    @Override
    public List<Action> findAll() {
        return actionRepository.findAll();
    }

    @Override
    public boolean save(Action action) {
        return actionRepository.save(action);
    }

    @Override
    public List<Action> findByDate(Long employeeId, Date date1, Date date2) {
        return actionRepository.findByDate(employeeId,date1,date2);
    }
}
