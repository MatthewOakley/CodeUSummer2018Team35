
package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;


public class HashtagTest {
  @Test
  public void testCreate() {
    UUID messageId = UUID.randomUUID();
    String name = "#TEST";
    
    Hashtag tag = new Hashtag(name, messageId);
    
    Assert.assertEquals(name, tag.getName());
    assert(tag.getMessageIds().contains(messageId));
  }
  
  @Test
  public void addId() {
    UUID messageIdOne = UUID.randomUUID();
    String name = "#TEST";
    
    Hashtag tag = new Hashtag(name, messageIdOne);
    UUID messageIdTwo = UUID.randomUUID();
    tag.addMessageId(messageIdTwo);
    
    Assert.assertEquals(name, tag.getName());
    assert(tag.getMessageIds().contains(messageIdOne));
    assert(tag.getMessageIds().contains(messageIdTwo));
  }
}