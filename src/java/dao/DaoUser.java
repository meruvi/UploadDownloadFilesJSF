package dao;

import java.util.ArrayList;
import java.util.List;
import model.User;

public class DaoUser {
    
    public List<User> findAllUser(){
        List<User> users = new ArrayList<User>();
        for(int i = 1; i <= 5; i++){
            User u = new User();
            u.setUser("juan" + i);
            u.setPass("juan" + i);
            
            users.add(u);
        }
        return users;
    }
    
}
