
package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class HashtagTest {
  @Test
  public void testCreate() {
    UUID messageId = UUID.randomUUID();
    String name = "#TEST";
    
    Hashtag tag = new Hashtag(name, messageId);
    
    Assert.assertEquals(name, tag.getName());
    UUID returnedMessageId = (tag.getMessageIds()).get(0);
    Assert.assertEquals(messageId, returnedMessageId);
  }
  
  @Test
  public void addId() {
    UUID messageIdOne = UUID.randomUUID();
    String name = "#TEST";
    
    Hashtag tag = new Hashtag(name, messageIdOne);
    UUID messageIdTwo = UUID.randomUUID();
    tag.addMessageId(messageIdTwo);
    
    Assert.assertEquals(name, tag.getName());
    UUID returnedMessageIdOne = tag.getMessageIds().get(0);
    Assert.assertEquals(messageIdOne, returnedMessageIdOne);
    UUID returnedMessageIdTwo = tag.getMessageIds().get(1);
    Assert.assertEquals(messageIdTwo, returnedMessageIdTwo);
  }
}