package ict.java;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 1. Get the ServletContext from the event
        ServletContext context = sce.getServletContext();

        // 2. Pull values using the <param-name> defined in web.xml
        String url = context.getInitParameter("dbUrl");
        String user = context.getInitParameter("dbUser");
        String password = context.getInitParameter("dbPassword");

        // 3. Pass these values into your StartUp method
        try {
            StartUp.startUp(url, user, password);
            System.out.println("Database initialized with URL: " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // This runs when the server shuts down (good for closing connection pools)
        System.out.println("Cleaning up resources...");
    }
}
