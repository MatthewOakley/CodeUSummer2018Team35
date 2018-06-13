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

import codeu.model.data.Mention;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class MentionStore {

  /** Singleton instance of MentionStore. */
  private static MentionStore instance;

  /**
   * Returns the singleton instance of MentionStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static MentionStore getInstance() {
    if (instance == null) {
      instance = new MentionStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static MentionStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new MentionStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Mentions from and saving Mentions to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Mentions. */
  private List<Mention> mentions;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private MentionStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    mentions = new ArrayList<>();
  }

  /**
   * Access the Mention object with the given name.
   *
   * @return null if username does not match any existing User.
   */
  public Mention getMention(String name) {
    // This approach will be pretty slow if we have many users.
    for (Mention mention : mentions) {
      if (mention.getMentionedUser().equals(name)) {
        return mention;
      }
    }
    return null;
  }

  /**
   * Access the  object with the given UUID.
   *
   * @return null if the UUID does not match any existing User.
   */
  public Mention getMention(UUID id) {
    for (Mention mention : mentionss) {
      if (mention.getConversationID().equals(id)) {
        return user;
      }
    }
    return null;
  }

  /**
   * Add a new user to the current set of users known to the application. This should only be called
   * to add a new user, not to update an existing user.
   */
  public void addMention(Mention mention) {
    mentions.add(mention);
    persistentStorageAgent.writeThrough(mention);
  }


  /** Return true if the given username is known to the application. */
  public boolean doesMentionExist(String name) {
    for (Mention mention : mentions) {
      if (mention.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets the List of Users stored by this UserStore. This should only be called once, when the data
   * is loaded from Datastore.
   */
  public void setMentions(List<Mention> mentions) {
    this.mentions = mentions;
  }
}

