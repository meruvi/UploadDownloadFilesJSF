package backingBean;

import controller.ControllerLogIn;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class beanIngreso {

    private String user;
    private String pass;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
    public beanIngreso() {
    }
    
    public String ingresoAlSistema(){
        ControllerLogIn controllerLogIn = new ControllerLogIn();
        if(controllerLogIn.LogIn(user, pass)){
            return "correcto";
        }else{
            return "error";
        }
    }
    
    public String retornarAlIndex(){
        return "indice";
    }
}
