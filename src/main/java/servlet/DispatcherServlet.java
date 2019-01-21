package servlet;

import controller.Controller;
import controller.HelloController;
import controller.RequestMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private Map<String, Method> uriMappings = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("Getting request for " + req.getRequestURI());
        String uri = req.getRequestURI();
        Method method = getMappingForUri(uri);
        if(method == null ){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        // TODO
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // on enregistre notre controller au démarrage de la servlet
        this.registerController(HelloController.class);
    }

    protected void registerController(Class controllerClass){
        System.out.println("Analysing class " + controllerClass.getName());
        if(controllerClass.getAnnotation(Controller.class) != null){
            for(int i = 0; i < controllerClass.getMethods().length; i++){
                if(controllerClass.getMethods()[i].getAnnotation(RequestMapping.class)!=null) {
                    uriMappings.put(controllerClass.getMethods()[i].getAnnotation(RequestMapping.class).uri(), controllerClass.getMethods()[i]);
                }
            }
        }
    }

    protected void registerMethod(Method method) {
        System.out.println("Registering method " + method.getName());
        // TODO
    }

    protected Map<String, Method> getMappings(){
        return this.uriMappings;
    }

    protected Method getMappingForUri(String uri){
        return this.uriMappings.get(uri);
    }
}