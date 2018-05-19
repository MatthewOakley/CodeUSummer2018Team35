// Admin Servlet

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

/**
 * AdminServlet 
 *
 * This servlet class is for the admin page
 */
public class AdminServlet extends HttpServlet {
  
  // setup the admin page
  @Override
  public void init() throws ServletException {
    super.init();
  }
  
  // when user visits sends them to admin page
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }
}