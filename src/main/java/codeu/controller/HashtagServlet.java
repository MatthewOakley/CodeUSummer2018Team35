package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.User;
import codeu.model.data.Message;
import codeu.model.data.Hashtag;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.HashtagStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This servlet class is for the hashtag portion of the project
 * it will get the hashtag the user is looking for and display all
 * corresponding messages that have that hashtag
 */
public class HashtagServlet extends HttpServlet {
  
  private HashtagStore hashtagStore;
  
  private MessageStore messageStore;
  
  // the length of /hashtag/
  private static final int HASHTAG_INDEX = 9;
  
  /** This is for the inital setup of the hashtag page */
  @Override
  public void init() throws ServletException {
    super.init();
    
    setHashtagStore(HashtagStore.getInstance());
    setMessageStore(MessageStore.getInstance());
  }
  
  /**
   * Sets the HashtagStore used by this servlet. 
   * This function provides a common setup method
   */
  void setHashtagStore(HashtagStore hashtagStore) {
    this.hashtagStore = hashtagStore;
  }
  
  /**
   * Sets the MessageStore used by this servlet. 
   * This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }
  
  /** When the user visits a hashtag page */
  @Override
  public void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws IOException, ServletException {
    
    String requestUrl = request.getRequestURI();

    String tagName = requestUrl.substring(HASHTAG_INDEX);

    Hashtag hashtag = hashtagStore.getHashtag(tagName);
    
    Set<UUID> messageIds = hashtag.getMessageIds();
    
    // TO-DO(Matthew Oakley) Figure out why it is not working
    //List<Message> messages = messageIds.stream().map(id -> messageStore.getMessageById(id)).collect(Collectors.toList());
    
    
    request.setAttribute("hashtagName", hashtag.getName());
    //request.setAttribute("messages", messages);
    
    request.getRequestDispatcher("/WEB-INF/view/hashtag.jsp").forward(request, response);
  
  }
}