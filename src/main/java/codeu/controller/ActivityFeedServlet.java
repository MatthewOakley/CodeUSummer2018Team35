package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the Activity Feed page. */
public class ActivityFeedServlet extends HttpServlet {

    /** Store class that gives access to Users. */
    private UserStore userStore;
    
    /**
     * Set up state for handling activity feed related requests.
     * This method is only called when running in a server, not when running in a test.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        setUserStore(UserStore.getInstance());
    }
    
    /**
     * Sets the UserStore used by this servlet.
     * This function provides a common setup method for use by the test framework or servlet's init() function.
     */
    void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }
    
    /**
     * This function firex when a user navigates to the activity feed page.
     * It forwards the request to activityfeed.jsp
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
            request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
    }
}
