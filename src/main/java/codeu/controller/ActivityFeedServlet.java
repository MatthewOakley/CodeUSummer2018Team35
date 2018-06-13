package codeu.controller;

import codeu.model.data.User;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import java.util.List;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the Activity Feed page. */
public class ActivityFeedServlet extends HttpServlet {

    /** Store class that gives access to Users. */
    private UserStore userStore;
    
    /** Store class that gives access to Conversations. */
    private ConversationStore conversationStore;
    
    /** Store class that gives access to Messages. */
    private MessageStore messageStore;
    
    /**
     * Set up state for handling activity feed related requests.
     * This method is only called when running in a server, not when running in a test.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        setUserStore(UserStore.getInstance());
        setConversationStore(ConversationStore.getInstance());
        setMessageStore(MessageStore.getInstance());
    }
    
    /**
     * Sets the UserStore used by this servlet.
     * This function provides a common setup method for use by the test
      framework or servlet's init() function.
     */
    void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }
    
    /**
     *Sets the ConversationStore used by this servlet.
     */
    void setConversationStore(ConversationStore conversationStore) {
        this.conversationStore = conversationStore;
    }
    
    /**
     *Sets the MessageStore used by this servlet.
     */
    void setMessageStore(MessageStore messageStore) {
        this.messageStore = messageStore;
    }
    
    /**
     * This function fires when a user navigates to the activity feed page.
     * It forwards the request to activityfeed.jsp
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
            List<Conversation> conversations = conversationStore.getAllConversations();
            request.setAttribute("conversations", conversations);
            request.getRequestDispatcher("/WEB-INF/view/activity-feed.jsp").forward(request, response);
    }
}
