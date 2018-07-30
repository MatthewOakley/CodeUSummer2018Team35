package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import codeu.model.data.Conversation;
import codeu.model.store.basic.ConversationStore;
import codeu.model.data.Message;
import codeu.model.store.basic.MessageStore;
import java.io.IOException;
import java.util.ArrayList;
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
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Scanner;

/** 
 * This servlet class is for the admin page
 * it will handle getting all the database information
 * and showing stats about it.
 */
public class AdminServlet extends HttpServlet {
   
  // Store class that gives access to Users. 
  private UserStore userStore;
  
  // Store class that gives access to Conversations.
  private ConversationStore conversationStore;
  
  // Store class that gives acces to messages
  private MessageStore messageStore;
  
  /** This is for the inital setup of the admin page */
  @Override
  public void init() throws ServletException {
    super.init();
    
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
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
   * Sets the ConversationStore used by this servlet. 
   * This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }
  
  /**
   * Sets the MessageStore used by this servlet. 
   * This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }
  
  /** When user visits this sends them to admin page */
  @Override
  public void doGet(HttpServletRequest request, 
      HttpServletResponse response)
      throws IOException, ServletException {
    
    int userCount = userStore.getUserAmount();
    request.setAttribute("userCount", userCount);
    
    int conversationCount = conversationStore.getConversationAmount();
    request.setAttribute("conversationCount", conversationCount);
    
    int messageCount = messageStore.getMessageCount();
    request.setAttribute("messageCount", messageCount);
    
    // get the newest user by creation time
    String newestUser = userStore.getNewestUser();
    request.setAttribute("newestUser", newestUser);
    
    // get the number of attacks on the website 
    File file = new File(System.getProperty("user.dir"), "attackLog.txt");
    Scanner input = new Scanner(file);
    int counter = 0;
    while (input.hasNextLine()) {
      input.nextLine();
      input.nextLine();
      counter++;
    }
    request.setAttribute("attackCount", counter);
    
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }
}
