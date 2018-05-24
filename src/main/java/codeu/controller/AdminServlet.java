package codeu.controller;

/** Getting the user stats for the webpage. */
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;

/** Getting the conversation stats for the webpage */
import codeu.model.data.Conversation;
import codeu.model.store.basic.ConversationStore;

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
 * This servlet class is for the admin page
 * it will handle getting all the database information
 * and showing stats about it.
 */
public class AdminServlet extends HttpServlet {
  /** 
   * TO-DO(Matthew Oakley) I need to add getting the data from the 
   * the database and checking to make sure its valid and not null
   * and sending this information to the webpage
   */
   
  /** Store class that gives access to Users. */
  private UserStore userStore;
  
  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;
  
  /** This is for the inital setup of the admin page */
  @Override
  public void init() throws ServletException {
    super.init();
    
    /** This is for getting the current instance of users */
    setUserStore(UserStore.getInstance());
    
    /** This is for getting the current instance of conversations */
    setConversationStore(ConversationStore.getInstance());
  }
  
  /**
   * Sets the UserStore used by this servlet. 
   * This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
  
  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }
  
  /** when user visits this sends them to admin page */
  @Override
  public void doGet(HttpServletRequest request, 
      HttpServletResponse response)
      throws IOException, ServletException {
    
    /** This gets the current number of users in the database*/
    int userCount = userStore.getUserAmount();
    request.setAttribute("userCount", userCount);
    
    /** This gets the amount of conversations in the database */
    int conversationCount = conversationStore.getConversationAmount();
    request.setAttribute("conversationCount", conversationCount);
    
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }
}
