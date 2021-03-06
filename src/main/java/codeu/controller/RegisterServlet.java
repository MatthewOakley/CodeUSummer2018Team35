package codeu.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.time.format.FormatStyle;
import java.time.ZoneId;
import com.google.appengine.api.datastore.Text;

public class RegisterServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /**
   * Set up state for handling registration-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String username = request.getParameter("username");
    String password = request.getParameter("password");
    boolean isAttack = false;

    if (username.contains(";") || username.contains("'") || username.contains("\"")) {
      isAttack = true;
    }

    if (isAttack) {
      String userAgent = "";
      File file = new File(System.getProperty("user.dir"), "attackLog.txt");
      BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
      try {
        userAgent = request.getHeader("User-Agent");
        // save the user info to a file
        writer.append(userAgent + "\n");

        // setting up the date and time of attack
        DateTimeFormatter formatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.US)
            .withZone(ZoneId.systemDefault());
        Instant instant = Instant.now();
        String time = formatter.format(instant);
        writer.append(time + "\n");
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        writer.close();
      }
    }

    if (!username.matches("[\\w*\\s*]*")) {
      request.setAttribute("error", "Please enter only letters, numbers, and spaces.");
      request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
      return;
    }

    if (userStore.isUserRegistered(username)) {
      request.setAttribute("error", "That username is already taken.");
      request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
      return;
    }

    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

    String aboutMe = request.getParameter("aboutMe");

    boolean isAdmin = username.equals("admin") && password.equals("admin");

    Text profilePic = null;

    User user = new User(UUID.randomUUID(), username, hashedPassword, Instant.now(), aboutMe, isAdmin, profilePic);

    userStore.addUser(user);

    response.sendRedirect("/login");
  }
}
