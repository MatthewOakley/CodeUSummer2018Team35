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

package codeu.model.store.basic;

import java.time.Instant;
import java.util.stream.Collectors;
import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class MessageStore {

  /** Singleton instance of MessageStore. */
  private static MessageStore instance;

  /**  Evaluates to the length of /users/ */
  private static final int USERNAME_INDEX = 7;

  /**
   * Returns the singleton instance of MessageStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static MessageStore getInstance() {
    if (instance == null) {
      instance = new MessageStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static MessageStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new MessageStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Messages from and saving Messages to
   * Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Messages. */
  private List<Message> messages;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private MessageStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    messages = new ArrayList<>();
  }

  /** Add a new message to the current set of messages known to the application. */
  public void addMessage(Message message) {
    messages.add(message);
    persistentStorageAgent.writeThrough(message);
  }

  /** Access the current set of Messages within the given Conversation. */
  public List<Message> getMessagesInConversation(UUID conversationId) {

    List<Message> messagesInConversation = new ArrayList<>();
    HashSet<UUID> messageIds = new HashSet<UUID>();

    for (Message message : messages) {
      if (message.getConversationId().equals(conversationId)) {
        if (!messageIds.contains(message.getId())) {
          messageIds.add(message.getId());
          messagesInConversation.add(message);
        } else {
          for (Message old : messagesInConversation) {
            if (old.getId().equals(message.getId())
                && old.getCreationTime().isBefore(message.getCreationTime())) {
              messagesInConversation.remove(old);
              messagesInConversation.add(message);
              break;
            }
          }
        }
      }
    }

    return messagesInConversation;
  }

/**
  private List<Message> getCurrentMessagesInConversation(UUID conversationId) {
    return messages.stream().filter(message -> message.getConversationId().equals(conversationId)).collect(Collectors.toList());
  }
    
  public List<Message> getMessagesInConversation(UUID conversationId) {
    List<Message> messages = getCurrentMessagesInConversation(conversationId);
    // Ensures messages are sorted in reverse chronological order (newest -> oldest): Please check if they are
    // somewhere before this. if they are, can remove
    Collections.sort(messages, (o1, o2) -> o2.getCreationTime().compareTo(o1.getCreationTime()));
    // Put into a tree set to get rid of all duplicate entries after the newest message (comparator looks at UUID
    // to do this), and then collects back into list
    return messages.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<UUID>(Comparator.comparing(UUID::toString))), ArrayList::new));
  }
*/

  /** Access the set of Messages sent by the user. */
  public List<Message> getMessagesByUser(UUID author) {

    return messages.stream().filter(m -> m.getAuthorId().equals(author)).collect(Collectors.toList());
  }

  /** Sets the List of Messages stored by this MessageStore. */
  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }
  
  /** Returns the size of the messages */
  public int getMessageCount() {
    return messages.size();
  }
    
  /** Access Message by UUID. */
  public Message getMessage(UUID messageId) {
      for (Message message : messages) {
          if (message.getId().equals(messageId)) {
              return message;
          }
      }
      return null;
  }

  /** Creates new message from old with new content then persists it. */
  public void editMessage(String messageId, String edit) {
    Message message = getMessage(UUID.fromString(messageId));
    Message editedMessage = new Message(message.getId(),
                                        message.getConversationId(),
                                        message.getAuthorId(),
                                        edit,
                                        Instant.now());
    messages.add(editedMessage);
    persistentStorageAgent.writeThrough(editedMessage);
  }
  
  /** Adds reply to parent message and persists. */
  public void reply(Message parent, Message reply) {
    parent.addReply(reply);
    persistentStorageAgent.writeThrough(parent);
  }
}
