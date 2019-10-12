package backingBean;

import controller.ControllerLogIn;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    
    public String downloadFile(String filename) {
        
        HttpServletRequest request   =(HttpServletRequest )FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String path = request.getServletContext().getInitParameter("pathSolicitudInversion");
        System.out.println("path:  " + path);
        
        System.out.println("FileName:  " + filename);
        //String filename = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("file");
        File file = new File(path + filename);
        System.out.println("PathFileName:  " + path + filename);
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();  

        writeOutContent(response, file, file.getName());

        FacesContext.getCurrentInstance().responseComplete();
        return null;
    }

    private void writeOutContent(final HttpServletResponse res, final File content, final String theFilename) {
        if (content == null) {
            System.out.println("Context: nulo");
            return;
        }
        try {
            res.setHeader("Pragma", "no-cache");
            res.setDateHeader("Expires", 0);
            res.setHeader("Content-disposition", "attachment; filename=" + theFilename);
            FileInputStream fis = new FileInputStream(content);
            ServletOutputStream os = res.getOutputStream();
            int bt = fis.read();
            while (bt != -1) {
                os.write(bt);
                bt = fis.read();
            }
            os.flush();
            fis.close();
            os.close();
        } catch (final IOException ex) {
            System.out.println("Error:  " + ex);
        }
    }
    
    public void descargaArchivo(String filename) {
        HttpServletRequest request   =(HttpServletRequest )FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String path = request.getServletContext().getInitParameter("pathSolicitudInversion");
        System.out.println("path:  " + path);
        
        // lo siento pero el estándar de programación dice que DEBEMOS declarar
        // nuestras variables al comienzo de cualquier función = (
        HttpServletResponse objResponse;
        FileInputStream objFileInputStream;
        String strNombreCompletoArchivo, strNombreArchivo;
        byte[] arrDatosArchivo;
        try {
            // Aquí obtengo el <f: param> con el nombre completo del archivo. Encapsula
            // la llamada Faces.getCurrentInstance ...
            //strNombreCompletoArchivo = UManejadorSesionWeb.obtieneParametro("nombreCompletoArchivo");
            strNombreCompletoArchivo = path + filename;
            // La función obtieneNombreArchivo recupera el nombre del archivo
            // basado en el nombre completo y el separador de archivos (/ para Windows, \ para Linux)
            //strNombreArchivo = UFuncionesGenerales.obtieneNombreArchivo(strNombreCompletoArchivo);
            strNombreArchivo = filename;
            // Obteniendo la respuesta de Faces.getCurrentInstance ...
            objResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            // Configurar el tipo de contenido de respuesta y el encabezado (¡no establezca la longitud!)
            objResponse.setContentType("application/octet-stream");
            objResponse.setHeader("Content-Disposition", "attachment; filename=\"" + strNombreArchivo + "\"");
            // Cree el FileInputStream para el archivo a descargar
            objFileInputStream = new FileInputStream(strNombreCompletoArchivo);
            // Configurando el archivo en la respuesta
            arrDatosArchivo = new byte[UConstante.BUFFER_SIZE];
            while(objFileInputStream.read(arrDatosArchivo, 0, UConstante.BUFFER_SIZE) != -1) {
               objResponse.getOutputStream().write(arrDatosArchivo, 0, UConstante.BUFFER_SIZE);
            }
            objFileInputStream.close();
            objResponse.getOutputStream().flush();
            objResponse.getOutputStream().close();
            // Indicando al framework que la respuesta ha sido completada.
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception objEx) {
            // gestiona los errores ...
        }
    }
    //The constant used for byte array size
    public class UConstante {
        public static final int BUFFER_SIZE = 2048;
    }
}
