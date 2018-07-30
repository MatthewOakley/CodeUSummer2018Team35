package codeu.model.store.basic;

import codeu.model.data.Mention;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MentionStoreTest {

  private MentionStore mentionStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final Mention MENTION_ONE =
      new Mention(
          UUID.randomUUID(),
          "test_username_one");

  private final Mention MENTION_TWO =
      new Mention(
          UUID.randomUUID(),
          "test_username_two");

  private final Mention MENTION_THREE =
      new Mention(
          UUID.randomUUID(),
          "test_username_three");

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    mentionStore = MentionStore.getTestInstance(mockPersistentStorageAgent);

    final List<Mention> mentionList = new ArrayList<Mention>();
    mentionList.add(MENTION_ONE);
    mentionList.add(MENTION_TWO);
    mentionList.add(MENTION_THREE);
    mentionStore.setMentions(mentionList);
  }


  @Test
  public void testGetMention_byUsername_found() {
    Mention resultMention = mentionStore.getMention(MENTION_ONE.getName());

    assertEquals(MENTION_ONE, resultMention);
  }

  @Test
  public void testSetConstructor() {
    Set<UUID> UUIDs = new HashSet<UUID>();
    UUIDs.add(UUID.randomUUID());
    UUIDs.add(UUID.randomUUID());
    Mention setMention = new Mention(UUIDs, "Mention_Set");

    mentionStore.addMention(setMention);
    Mention resultMention = mentionStore.getMention("Mention_Set");

    assertEquals(setMention, resultMention);
  }

  @Test
  public void testGetMention_byUsername_notFound() {
    Mention resultMention = mentionStore.getMention("fake mention");

    Assert.assertNull(resultMention);
  }

  @Test
  public void testAddMention() {
    Mention inputMention =
        new Mention(
            UUID.randomUUID(),
            "test_mention");

    mentionStore.addMention(inputMention);
    Mention resultMention = mentionStore.getMention("test_mention");

    assertEquals(inputMention, resultMention);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputMention);
  }

  @Test
  public void testIsPresent_true() {
    Assert.assertTrue(mentionStore.isPresent(MENTION_ONE.getName()));
  }

  @Test
  public void testIsPresent_false() {
    Assert.assertFalse(mentionStore.isPresent("fake username"));
  }

  private void assertEquals(Mention expectedMention, Mention actualMention) {

    Assert.assertEquals(expectedMention.getName(), actualMention.getName());

    Set<UUID> mentionOne = expectedMention.getMessageIds();
    Set<UUID> mentionTwo = actualMention.getMessageIds();

    for (UUID id : mentionOne) {
      assert(mentionTwo.contains(id));
    }
  }
}
