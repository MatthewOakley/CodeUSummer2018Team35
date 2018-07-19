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
import com.google.appengine.repackaged.com.google.api.client.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

/** Servlet class responsible for the profile pages. */
@MultipartConfig(maxFileSize=10*1024*1024)
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

    Part filePart = request.getPart("file");
    InputStream fileContent = filePart.getInputStream();
    String imageString = encodeString(fileContent);

    user.setImageString(imageString);
    if (user == null) {
        response.sendRedirect("/login");
        return;
    }

    String aboutMeContent = request.getParameter("aboutMe");

    // this removes any HTML from the about me content
    String cleanedAboutMeContent = Jsoup.clean(aboutMeContent, Whitelist.none());

    user.setAboutMe(cleanedAboutMeContent);
    UserStore.getInstance().updateUser(user);
    response.sendRedirect("/users/" + username);

  }

  private String encodeString(InputStream fileInputStreamReader) {
    String resultEncoded = null;
    try {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int zero = 0;
      while ((zero = fileInputStreamReader.read(buffer, 0, buffer.length)) != -1) {
        output.write(buffer, 0, zero);
      }
      output.flush();

      byte[] bytes = output.toByteArray();
      fileInputStreamReader.read(bytes);
      resultEncoded = new String(Base64.encodeBase64(bytes), "UTF-8");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return resultEncoded;
  }
}
