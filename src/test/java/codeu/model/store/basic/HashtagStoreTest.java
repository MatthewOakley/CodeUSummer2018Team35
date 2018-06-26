package codeu.model.store.basic;

import codeu.model.data.Hashtag;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class HashtagStoreTest {

  private HashtagStore hashtagStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final Hashtag TAG_ONE =
      new Hashtag(
          "HASHTAG_ONE",
          UUID.randomUUID());
          
  private final Hashtag TAG_TWO =
      new Hashtag(
          "HASHTAG_TWO",
          UUID.randomUUID());
  
  private final Hashtag TAG_THREE =
      new Hashtag(
          "HASHTAG_THREE",
          UUID.randomUUID());

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    hashtagStore = HashtagStore.getTestInstance(mockPersistentStorageAgent);

    final List<Hashtag> hashtagList = new ArrayList<Hashtag>();
    hashtagList.add(TAG_ONE);
    hashtagList.add(TAG_TWO);
    hashtagList.add(TAG_THREE);
    hashtagStore.setHashtags(hashtagList);
  }

  @Test
  public void testSetConstructor() {
    Set<UUID> UUIDs = new HashSet<UUID>();
    UUIDs.add(UUID.randomUUID());
    UUIDs.add(UUID.randomUUID());
    Hashtag setTag = new Hashtag("HASHTAG_SET", UUIDs);
    
    hashtagStore.addHashtag(setTag);
    Hashtag resultHashtag = hashtagStore.getHashtag("HASHTAG_SET");
    
    assertEquals(setTag, resultHashtag);
  }
  
  @Test
  public void testGetHashtag_byName_found() {
    Hashtag resultHashtag = hashtagStore.getHashtag(TAG_ONE.getName());

    assertEquals(TAG_ONE, resultHashtag);
  }

  @Test
  public void testGetHashtag_byName_notFound() {
    Hashtag resultHashtag = hashtagStore.getHashtag("fake hashtag");

    Assert.assertNull(resultHashtag);
  }

  @Test
  public void testAddHashtag() {
    Hashtag inputHashtag =
        new Hashtag(
            "INPUT_HASHTAG",
            UUID.randomUUID());

    hashtagStore.addHashtag(inputHashtag);
    Hashtag resultHashtag = hashtagStore.getHashtag("INPUT_HASHTAG");

    assertEquals(inputHashtag, resultHashtag);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputHashtag);
  }

  @Test
  public void testisPresent_true() {
    Assert.assertTrue(hashtagStore.isPresent(TAG_ONE.getName()));
  }

  @Test
  public void testisPresent_false() {
    Assert.assertFalse(hashtagStore.isPresent("fake hashtag"));
  }

  private void assertEquals(Hashtag expectedHashtag, Hashtag actualHashtag) {
    Assert.assertEquals(expectedHashtag.getName(), actualHashtag.getName());
    
    Set<UUID> tagOne = expectedHashtag.getMessageIds();
    Set<UUID> tagTwo = actualHashtag.getMessageIds();
    
    for (UUID id : tagOne) {
      assert(tagTwo.contains(id));
    }
  }
}