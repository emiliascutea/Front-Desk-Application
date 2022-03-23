package model.builder;

import model.Action;

import java.sql.Date;

public class ActionBuilder {
    private Action action;

    public ActionBuilder() {
        action = new Action();
    }

    public ActionBuilder setActionId(Long id){
        action.setActionId(id);
        return this;
    }

    public ActionBuilder setEmployeeId(Long id){
        action.setEmployeeId(id);
        return this;
    }

    public ActionBuilder setName(String name){
        action.setName(name);
        return this;
    }

    public ActionBuilder setDateOfCreation(Date dateOfCreation){
        action.setDateOfCreation(dateOfCreation);
        return this;
    }

    public Action build(){
        return action;
    }
}
