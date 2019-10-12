package controller;

import dao.DaoUser;
import model.User;

public class ControllerLogIn {
    
    public boolean LogIn(String user, String pass){
        DaoUser daoUser = new DaoUser();
        
        for(User u: daoUser.findAllUser()){
            if(u.getUser().equals(user) && u.getPass().equals(pass)){
                return true;
            }
        }
        return false;
    }
    
}
