package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import java.io.File;
import java.io.FileOutputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Set;

import java.time.LocalDate;
import java.time.Period;


import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Part;

import org.mindrot.jbcrypt.BCrypt;

import com.google.appengine.api.datastore.Text;
import com.google.appengine.repackaged.com.google.common.io.Files;

/** Servlet class responsible for the profile pages. */
@MultipartConfig
public class ProfilePagesServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /**  Evaluates to the length of /users/ */
  private static final int USERNAME_INDEX = 7;

  /**
   * Set up state for handling profile page requests.
   * This method is only called when running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setMessageStore(MessageStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * This function fires when a user goes to the profile pages.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String username = requestUrl.substring(USERNAME_INDEX);
    User user = userStore.getUser(username);
    UUID userId = userStore.getUser(username).getId();

    List<Message> messages = messageStore.getMessagesByUser(userId);

    request.setAttribute("messages", messages);
    request.setAttribute("user", user);
    request.setAttribute("username", username);
    request.getRequestDispatcher("/WEB-INF/view/profile-pages.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String action = (String) request.getParameter("EditProfilePage");
    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    String requestUrl = request.getRequestURI();

    if (action != null && action.equals("EditProfilePicture")) {
      System.out.println(action);
      Part file = request.getPart("pic");

      InputStream content = file.getInputStream();

		  byte[] buffer = new byte[content.available()];
		  content.read(buffer, 0, content.available());

		  String base64 = Base64.getEncoder().encodeToString(buffer);
		  Text t = new Text(base64);
		  user.setProfilePic(t);

      UserStore.getInstance().updateUser(user);
      response.sendRedirect("/users/" + username);
    }

    String aboutMeContent = request.getParameter("aboutMe");

    // this removes any HTML from the about me content
    String cleanedAboutMeContent = Jsoup.clean(aboutMeContent, Whitelist.none());

    user.setAboutMe(cleanedAboutMeContent);
    UserStore.getInstance().updateUser(user);
    response.sendRedirect("/users/" + username);

  }
}
