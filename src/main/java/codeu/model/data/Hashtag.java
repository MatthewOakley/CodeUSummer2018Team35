package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

/** Class representing a registered hashtag. */
public class Hashtag {
  private final String hashtag;
  
  /** The list of message ids for this hashtag. */
  private List<UUID> messageIds;
  
  /** Setup the hashtag object */
  public Hashtag(String tag, UUID initialId) {
    this.hashtag = tag.toUpperCase();
    this.messageIds = new ArrayList<UUID>();
    this.messageIds.add(initialId);
  }
  
  /** Setup when making a hashtag from datastore */
  public Hashtag(String tag, List<UUID> messageIdsList) {
    this.hashtag = tag.toUpperCase();
    this.messageIds = messageIdsList;
  }
  
  /** This will add a message to the hashtag */
  public void addMessageId(UUID id) {
    
    if (messageIds.contains(id)) {
      return;
    }
    
    messageIds.add(id);
    return;
  }
  
  /** This will return the name of the hashtag */
  public String getName() {
    return hashtag;
  }
  
  /** This will return an array of the ids that are linked to this hashtag*/
  public List<UUID> getMessageIds() {
    return messageIds;
  }
}
