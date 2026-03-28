
import ict.db.CustomerDB;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/checkUsername")
public class CheckUsernameServlet extends HttpServlet {

    private CustomerDB db;

    @Override
    public void init() {
        String dbUser = "root";
        String dbPassword = "";
        String dbUrl = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
        db = new CustomerDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");

        PrintWriter out = response.getWriter();

        if (username == null || username.trim().isEmpty()) {
            out.print("{\"available\":false, \"message\":\"Username is required.\"}");
            out.flush();
            return;
        }

        username = username.trim();

        // Optional: enforce minimum length
        if (username.length() < 4) {
            out.print("{\"available\":false, \"message\":\"Username must be at least 4 characters.\"}");
            out.flush();
            return;
        }

        boolean isTaken = db.isUsernameTaken(username);

        System.out.println(username);
        System.out.println(isTaken);

        // Minimal JSON response – easy to parse in JavaScript
        if (isTaken) {
            out.print("{\"available\":false, \"message\":\"Username is already taken.\"}");
        } else {
            out.print("{\"available\":true, \"message\":\"Username is available.\"}");
        }

        out.flush();
    }
}
