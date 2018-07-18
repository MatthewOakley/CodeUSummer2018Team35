// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
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
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;


public class ImageUploadServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
 
   /** Store class that gives access to Conversations. */
    private ConversationStore conversationStore;

   /** Store class that gives access to Messages. */
    private MessageStore messageStore;

   /** Store class that gives access to Users. */
    private UserStore userStore;

   /** Set up state for handling chat requests. */
   @Override
   public void init() throws ServletException {
     super.init();
     setConversationStore(ConversationStore.getInstance());
     setMessageStore(MessageStore.getInstance());
     setUserStore(UserStore.getInstance());
   }

   /**
      * Sets the ConversationStore used by this servlet. This function provides a common setup method
      * for use by the test framework or the servlet's init() function.
      */
   void setConversationStore(ConversationStore conversationStore) {
     this.conversationStore = conversationStore;
   }

   /**
      * Sets the MessageStore used by this servlet. This function provides a common setup method for
      * use by the test framework or the servlet's init() function.
      */
   void setMessageStore(MessageStore messageStore) {
     this.messageStore = messageStore;
   }

   /**
      * Sets the UserStore used by this servlet. This function provides a common setup method for use
      * by the test framework or the servlet's init() function.
      */
   void setUserStore(UserStore userStore) {
     this.userStore = userStore;
   }
   
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("user");
        if (username == null) {
         // user is not logged in, don't let them add a message
           response.sendRedirect("/login");
           return;
        }

     User user = userStore.getUser(username);
     if (user == null) {
         // user was not found, don't let them add a message
         System.out.println("User not found: " + username);
         response.sendRedirect("/login");
         return;
     }

     String conversationTitle = request.getParameter("conversationTitle");
     Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
     if (conversation == null) {
         // couldn't find conversation, redirect to conversation list
         System.out.println("Conversation is not found");
         response.sendRedirect("/conversations");
         return;
     }
     
     Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("myFile");
          String url = "";
   
          if (!(blobKeys == null || blobKeys.isEmpty())) {
            url = "/messageServe?blob-key=" + blobKeys.get(0).getKeyString();
          }
        
     if(url != ""){
       System.out.println("The URL is:" + url);
       Message message =
         new Message(
             UUID.randomUUID(),
             conversation.getId(),
             user.getId(),
             url,
             Instant.now(),
             "image");

       messageStore.addMessage(message);
     }
   
     // redirect to a GET request
     response.sendRedirect("/chat/" + conversationTitle);
    }
} 