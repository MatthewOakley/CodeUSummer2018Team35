package codeu.model.store.basic;

import codeu.model.data.Hashtag;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
  public void testListConstructor() {
    List<UUID> uuidList = new ArrayList<UUID>();
    uuidList.add(UUID.randomUUID());
    uuidList.add(UUID.randomUUID());
    Hashtag listTag = new Hashtag("HASHTAG_LIST", uuidList);
    
    hashtagStore.addHashtag(listTag);
    Hashtag resultHashtag = hashtagStore.getHashtag("HASHTAG_LIST");
    
    assertEquals(listTag, resultHashtag);
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
  public void testDoesHashtagExist_true() {
    Assert.assertTrue(hashtagStore.doesHashtagExist(TAG_ONE.getName()));
  }

  @Test
  public void testDoesHashtagExist_false() {
    Assert.assertFalse(hashtagStore.doesHashtagExist("fake hashtag"));
  }

  private void assertEquals(Hashtag expectedHashtag, Hashtag actualHashtag) {
    Assert.assertEquals(expectedHashtag.getName(), actualHashtag.getName());
    
    List<UUID> tagOne = expectedHashtag.getMessageIds();
    List<UUID> tagTwo = actualHashtag.getMessageIds();
    
    for (UUID id : tagOne) {
      assert(tagTwo.contains(id));
    }
  }
}