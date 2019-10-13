package backingBean;

import controller.ControllerLogIn;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ManagedBean
@SessionScoped
public class beanIngreso {

    private String user;
    private String pass;
    private List<String> archivosAdjuntos = new ArrayList<String>();

    public List<String> getArchivosAdjuntos() {
        archivosAdjuntos.clear();
        archivosAdjuntos.add("prueba.apk");
        archivosAdjuntos.add("prueba.exe");
        archivosAdjuntos.add("prueba.jar");
        archivosAdjuntos.add("prueba.jpg");
        archivosAdjuntos.add("prueba.json");
        archivosAdjuntos.add("prueba.mdb");
        archivosAdjuntos.add("prueba.mp4");
        archivosAdjuntos.add("prueba.pdf");
        archivosAdjuntos.add("prueba.png");
        archivosAdjuntos.add("prueba.rar");
        archivosAdjuntos.add("prueba.sql");
        archivosAdjuntos.add("prueba.xls");
        return archivosAdjuntos;
    }

    public void setArchivosAdjuntos(List<String> archivosAdjuntos) {
        this.archivosAdjuntos = archivosAdjuntos;
    }

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
    
    public String downloadFile() {
        
        HttpServletRequest request   =(HttpServletRequest )FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String path = request.getServletContext().getInitParameter("pathSolicitudInversion");
        System.out.println("path:  " + path);
        
        String filename = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("archivo");
        File file = new File(path + filename);
        System.out.println("FileName:  " + filename);
        
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
    
    public void downloadArchivo() {
        
        //recuperamos la ruta absoluta desde web.xml
        HttpServletRequest request   =(HttpServletRequest )FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String path = request.getServletContext().getInitParameter("pathSolicitudInversion");
        System.out.println("path:  " + path);
        
        //Recuperamos el parametro enviado "nombre del archivo"
        Map<String, String> parameterMap = (Map<String, String>) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String filename = parameterMap.get("archivo");
        System.out.println("Archivo:  " + filename);
        
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
        } catch (Exception ex) {
            System.out.println("Error:  " + ex);
        }
    }
    
    public void descargarArchivo(){
        ExternalContext context   =FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest req = (HttpServletRequest) context.getRequest();
        Map<String, String> param = (Map<String, String>) context.getRequestParameterMap();
        HttpServletResponse objResponse = (HttpServletResponse) context.getResponse();
        FileInputStream objFileInputStream;
        byte[] arrDatosArchivo;
        
        String ruta = req.getServletContext().getInitParameter("pathSolicitudInversion");
        String archivo = param.get("archivo");
        String rutaCompleta = ruta + archivo;
        
        try {
            objResponse.setContentType("application/octet-stream");
            objResponse.setHeader("Content-Disposition", "attachment; filename=\"" + archivo + "\"");
            objFileInputStream = new FileInputStream(rutaCompleta);
            arrDatosArchivo = new byte[UConstante.BUFFER_SIZE];
            while(objFileInputStream.read(arrDatosArchivo, 0, UConstante.BUFFER_SIZE) != -1) {
               objResponse.getOutputStream().write(arrDatosArchivo, 0, UConstante.BUFFER_SIZE);
            }
            objFileInputStream.close();
            objResponse.getOutputStream().flush();
            objResponse.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            System.out.println("Error:  " + ex);
        }
    }
    
    public class UConstante {
        public static final int BUFFER_SIZE = 2048;
    }
}
