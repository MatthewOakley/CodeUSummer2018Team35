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
import codeu.model.data.Mention;
import codeu.model.store.basic.MentionStore;
import codeu.model.data.Hashtag;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.HashtagStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import com.vdurmont.emoji.EmojiParser;
import java.util.regex.Matcher;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

/** Servlet class responsible for the chat page. */
public class ChatServlet extends HttpServlet {

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;
  
  private HashtagStore hashtagStore;

  /** Store class that gives access to Mentions. */
  private MentionStore mentionStore;

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
    setMentionStore(MentionStore.getInstance());
    setHashtagStore(HashtagStore.getInstance());
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

   /**
   * Sets the MentionStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setMentionStore(MentionStore mentionStore) {
    this.mentionStore = mentionStore;
  }

  /**
   * Sets the HashtagStore used by this servlet. 
   * This function provides a common setup method
   */
  void setHashtagStore(HashtagStore hashtagStore) {
    this.hashtagStore = hashtagStore;
  }
  
  /**
   * This function fires when a user navigates to the chat page. It gets the conversation title from
   * the URL, finds the corresponding Conversation, and fetches the messages in that Conversation.
   * It then forwards to chat.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      System.out.println("Conversation was null: " + conversationTitle);
      response.sendRedirect("/conversations");
      return;
    }

    UUID conversationId = conversation.getId();

    List<Message> messages = messageStore.getMessagesInConversation(conversationId);

    request.setAttribute("conversation", conversation);
    request.setAttribute("messages", messages);
    request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the chat page. It gets the logged-in
   * username from the session, the conversation title from the URL, and the chat message from the
   * submitted form data. It creates a new Message from that data, adds it to the model, and then
   * redirects back to the chat page.
   */
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
    String conversationTitle = requestUrl.substring("/chat/".length());
          
    String edit = request.getParameter("edit");
    if (edit != null) {
      edit = Jsoup.clean(edit, Whitelist.none());
      edit = EmojiParser.parseToUnicode(edit);
      messageStore.editMessage((String) request.getParameter("messageId"), edit);
      response.sendRedirect("/chat/" + conversationTitle);
      return;
    }

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      response.sendRedirect("/conversations");
      return;
    }
    
    boolean shouldDelete = Boolean.valueOf(request.getParameter("delete"));
    if (shouldDelete) {
      messageStore.deleteMessage(messageStore.getMessage(UUID.fromString(request.getParameter("messageId"))));
      response.sendRedirect("/chat/" + conversationTitle);
      return;
    }
    
    String messageContent = request.getParameter("message");

    UUID messageUUID = UUID.randomUUID();
    
    // this removes any HTML from the message content
    String cleanedMessageContent = Jsoup.clean(messageContent, Whitelist.none());

    String cleanedAndEmojiMessage = EmojiParser.parseToUnicode(cleanedMessageContent);


    Pattern mentionPattern = Pattern.compile("@[^@]+(\\s|\\n|$)");
    Matcher mentionMatch = mentionPattern.matcher(cleanedAndEmojiMessage);
    Set<String> mentionedUsers = new HashSet<String>();

    while (mentionMatch.find()) {
      String mentionedUser = mentionMatch.group().trim().substring(1);
      mentionedUsers.add(mentionedUser);
    }

    for (String mentionedUser : mentionedUsers) {
      Mention currentMention = mentionStore.getMention(mentionedUser);
      if (currentMention == null) {
        currentMention = new Mention(messageUUID, mentionedUser);
        mentionStore.addMention(currentMention);
      } else {
        currentMention.addMessageId(messageUUID);
        mentionStore.updateMention(currentMention);
      }
    }
  

    Pattern hashtagPattern = Pattern.compile("(?:^|\\s|\\n)#([a-z\\d-]+)");
    Matcher matcher = hashtagPattern.matcher(cleanedAndEmojiMessage);
    
    Set<String> hashtags = new HashSet<String>();
    
    while (matcher.find()) {
      String tag = matcher.group().trim().substring(1);
      hashtags.add(tag);
    }
    
    for (String tag : hashtags) {
      tag = tag.toUpperCase();
      Hashtag currentTag = hashtagStore.getHashtag(tag);
      
      if (currentTag == null) {
        currentTag = new Hashtag(tag, messageUUID);
        hashtagStore.addHashtag(currentTag);
      } else {
        currentTag.addMessageId(messageUUID);
        hashtagStore.updateHashtag(currentTag);
      }
    }
    
    Message message =
        new Message(
            messageUUID,
            conversation.getId(),
            user.getId(),
            cleanedAndEmojiMessage,
            Instant.now(), 
            "text");

    Boolean isReply = Boolean.valueOf(request.getParameter("reply"));
    if (isReply == null || !isReply) {
      messageStore.addMessage(message);
    } else {
      messageStore.reply(messageStore.getMessage(UUID.fromString(request.getParameter("messageId"))),
                         message);
    }

    // redirect to a GET request
    response.sendRedirect("/chat/" + conversationTitle);
  }
}
