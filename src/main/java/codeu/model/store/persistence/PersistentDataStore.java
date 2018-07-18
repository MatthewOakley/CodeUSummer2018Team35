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

package codeu.model.store.persistence;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Mention;
import codeu.model.data.Hashtag;
import codeu.model.store.persistence.PersistentDataStoreException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * This class handles all interactions with Google App Engine's Datastore service. On startup it
 * sets the state of the applications's data objects from the current contents of its Datastore. It
 * also performs writes of new of modified objects back to the Datastore.
 */
public class PersistentDataStore {

  // Handle to Google AppEngine's Datastore service.
  private DatastoreService datastore;

  /**
   * Constructs a new PersistentDataStore and sets up its state to begin loading objects from the
   * Datastore service.
   */
  public PersistentDataStore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /**
   * Loads all User objects from the Datastore service and returns them in a List.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<User> loadUsers() throws PersistentDataStoreException {

    List<User> users = new ArrayList<>();

    // Retrieve all users from the datastore.
    Query query = new Query("chat-users");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        String userName = (String) entity.getProperty("username");
        String passwordHash = (String) entity.getProperty("password_hash");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String aboutMe = (String) entity.getProperty("aboutMe");
        boolean adminStatus = (boolean) entity.getProperty("adminStatus");
        User user = new User(uuid, userName, passwordHash, creationTime, aboutMe, adminStatus);
        users.add(user);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return users;
  }

  /**
   * Loads all Conversation objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Conversation> loadConversations() throws PersistentDataStoreException {

    List<Conversation> conversations = new ArrayList<>();

    // Retrieve all conversations from the datastore.
    Query query = new Query("chat-conversations").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID ownerUuid = UUID.fromString((String) entity.getProperty("owner_uuid"));
        String title = (String) entity.getProperty("title");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        Conversation conversation = new Conversation(uuid, ownerUuid, title, creationTime);
        conversations.add(conversation);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return conversations;
  }

  /**
   * Loads all Message objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Message> loadMessages() throws PersistentDataStoreException {

    List<Message> messages = new ArrayList<>();

    // Retrieve all messages from the datastore.
    Query query = new Query("chat-messages").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID conversationUuid = UUID.fromString((String) entity.getProperty("conv_uuid"));
        UUID authorUuid = UUID.fromString((String) entity.getProperty("author_uuid"));
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String content = (String) entity.getProperty("content");
        String parentId = (String) entity.getProperty("parent");
        Message message = new Message(uuid, conversationUuid, authorUuid, content, creationTime);
        if (parentId != null) {
          for (Message parent : messages) {
            if (parentId.equals(parent.getId().toString())) {
              parent.addReply(message);
              break;
            }
          }
        } else {
          messages.add(message);
        }
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return messages;
  }

  /**
   * Loads all Mention objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Mention> loadMentions() throws PersistentDataStoreException {

    List<Mention> mentions = new ArrayList<>();

    // Retrieve all messages from the datastore.
    Query query = new Query("chat-mentions");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {

        Set<String> dataStoreMessageIds = new HashSet<>((Collection<String>)
          entity.getProperty("uuid_list"));
        Set<UUID> messageIds = dataStoreMessageIds.stream().map(id -> UUID.fromString(id)).collect(Collectors.toSet());
        String mentionedUser = (String) entity.getProperty("mentioned_user");
        Mention mention = new Mention(messageIds, mentionedUser);
        mentions.add(mention);

      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return mentions;
  }

   /**
   * Loads all Hashtag objects from the Datastore service and returns them in a List.
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Hashtag> loadHashtags() throws PersistentDataStoreException {

    List<Hashtag> hashtags = new ArrayList<>();

    // Retrieve all hashtags from the datastore.
    Query query = new Query("chat_hashtags");

    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String tagName = (String) entity.getProperty("tag_name");
        List<String> datastoreMessageIds = new ArrayList( (Collection<String>) entity.getProperty("uuid_list"));
        Set<UUID> messageIds = datastoreMessageIds.stream().map(id -> UUID.fromString(id)).collect(Collectors.toSet());
        Hashtag hashtag = new Hashtag(tagName, messageIds);
        hashtags.add(hashtag);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return hashtags;
  }

  /** Write a User object to the Datastore service. */
  public void writeThrough(User user) {
    Entity userEntity = new Entity("chat-users", user.getId().toString());
    userEntity.setProperty("uuid", user.getId().toString());
    userEntity.setProperty("username", user.getName());
    userEntity.setProperty("password_hash", user.getPasswordHash());
    userEntity.setProperty("creation_time", user.getCreationTime().toString());
    userEntity.setProperty("aboutMe", user.getAboutMe());
    userEntity.setProperty("adminStatus", user.isAdmin());
    datastore.put(userEntity);
  }

  /** Write a Message object to the Datastore service. */
  public void writeThrough(Message message) {
    Entity messageEntity = new Entity("chat-messages", message.getId().toString());
    messageEntity.setProperty("uuid", message.getId().toString());
    messageEntity.setProperty("conv_uuid", message.getConversationId().toString());
    messageEntity.setProperty("author_uuid", message.getAuthorId().toString());
    messageEntity.setProperty("content", message.getContent());
    messageEntity.setProperty("creation_time", message.getCreationTime().toString());
    for (Message reply : message.getReplies()) {
      datastore.put(getReplyEntity(message, reply));
    }
    datastore.put(messageEntity);

  }

  /** Remove a Message object from the Datastore service. */
  public void deleteThrough(Message message){
    Key messageKey = KeyFactory.createKey("chat-messages", message.getId().toString());
    datastore.delete(messageKey);
  }

  /** Creates an entity for a reply that contains the UUID of its parent. */
  public Entity getReplyEntity(Message parent, Message reply) {
    Entity messageEntity = new Entity("chat-messages", reply.getId().toString());
    messageEntity.setProperty("uuid", reply.getId().toString());
    messageEntity.setProperty("conv_uuid", reply.getConversationId().toString());
    messageEntity.setProperty("author_uuid", reply.getAuthorId().toString());
    messageEntity.setProperty("content", reply.getContent());
    messageEntity.setProperty("creation_time", reply.getCreationTime().toString());
    messageEntity.setProperty("parent", parent.getId().toString());
    return messageEntity;
  }

  /** Write a Conversation object to the Datastore service. */
  public void writeThrough(Conversation conversation) {
    Entity conversationEntity = new Entity("chat-conversations", conversation.getId().toString());
    conversationEntity.setProperty("uuid", conversation.getId().toString());
    conversationEntity.setProperty("owner_uuid", conversation.getOwnerId().toString());
    conversationEntity.setProperty("title", conversation.getTitle());
    conversationEntity.setProperty("creation_time", conversation.getCreationTime().toString());
    datastore.put(conversationEntity);
  }

  /** Write a Conversation object to the Datastore service. */
  public void writeThrough(Mention mention) {
    Entity mentionEntity = new Entity("chat-mentions", mention.getMentionedUser());
    mentionEntity.setProperty("mentioned_user", mention.getMentionedUser());
    Collection<String> messageIds = mention.getMessageIds().stream().map(id -> id.toString()).collect(Collectors.toList());
    mentionEntity.setProperty("uuid_list", messageIds);
    datastore.put(mentionEntity);
  }

  /** Remove a Conversation object from the Datastore service. */
  public void deleteThrough(Conversation conversation){
    Key conversationKey = KeyFactory.createKey("chat-conversations", conversation.getId().toString());
    datastore.delete(conversationKey);
  }

  /** Write a Hashtag object to the Datastore service. */
  public void writeThrough(Hashtag hashtag) {
    Entity hashtagEntity = new Entity("chat_hashtags", hashtag.getName());
    hashtagEntity.setProperty("tag_name", hashtag.getName());
    Collection<String> messageIds = hashtag.getMessageIds().stream().map(id -> id.toString()).collect(Collectors.toList());
    hashtagEntity.setProperty("uuid_list", messageIds);
    datastore.put(hashtagEntity);
  }
}
