package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import java.util.List;

/** Class representing a registered hashtag. */
public class Hashtag {
  private final String hashtag;
  
  /** The in-memory list of message ids for this hashtag. */
  private List<UUID> messageIds;
  
  /** Setup the hashtag object */
  public Hashtag(String tag) {
    this.hashtag = tag.toUpperCase();
  }
  
  /** This will add a message to the hashtag */
  public void addMessageId(UUID id) {
    
    if (messageIds.contains(id)) {
      return;
    }
    
    messageIds.add(id);
    return;
  }
}
