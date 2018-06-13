package codeu.model.store.basic;

import codeu.model.data.Hashtag;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Collections;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class HashtagStore {
  
  /** Singleton instance of HashtagStore. */
  private static HashtagStore instance;
  
  /** The in-memory list of Users. */
  private List<Hashtag> hashtags;
  
  /**
   * Returns the singleton instance of HashtagStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static HashtagStore getInstance() {
    if (instance == null) {
      instance = new HashtagStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static HashtagStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new HashtagStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Hashtags from and saving Hashtags to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private HashtagStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    hashtags = new ArrayList<>();
  }

  /**
   * Access the Hashtag object with a name.
   *
   * @return null if there is not hashtag with that name.
   */
  public Hashtag getHashtag(String name) {
    // This approach will be pretty slow if we have many Hashtags.
    for (Hashtag hashtag : hashtags) {
      if (hashtag.getName().equals(name)) {
        return hashtag;
      }
    }
    return null;
  }

  /**
   * Add a new hashtag to the current set of hashtags known to the application. This should only be called
   * to add a new hashtag, not to update an existing hashtag.
   */
  public void addHashtag(Hashtag hashtag) {
    if (doesHashtagExist(hashtag.getName())) {
      return;
    }
    
    hashtags.add(hashtag);
    persistentStorageAgent.writeThrough(hashtag);
  }

  /**
   * Update an existing Hashtag.
   */
  public void updateHashtag(Hashtag hashtag) {
    if (doesHashtagExist(hashtag.getName())) {
      persistentStorageAgent.writeThrough(hashtag);
    }
  }

  /** Return true if the given Hashtag exists. */
  public boolean doesHashtagExist(String name) {
    for (Hashtag hashtag : hashtags) {
      if (hashtag.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets the List of Hashtags stored by this HashtagStore. This should only be called once, when the data
   * is loaded from Datastore.
   */
  public void setHashtags(List<Hashtag> hashtags) {
    this.hashtags = hashtags;
  }
}

