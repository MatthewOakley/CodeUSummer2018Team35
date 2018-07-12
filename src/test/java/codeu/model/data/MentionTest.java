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

package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import java.util.Set;
import java.util.HashSet;


public class MentionTest {

  @Test
  public void testCreate() {
    UUID messageId = UUID.randomUUID();
    String mentionedUser = "test_username";
    
    Mention mention = new Mention(messageId, mentionedUser);

    
    Assert.assertEquals(mentionedUser, mention.getName());
    assert(mention.getMessageIds().contains(messageId));
  }

  @Test
 public void addId() {
    UUID messageIdOne = UUID.randomUUID();
    String name = "@test";
    
    Mention mention = new Mention(messageIdOne, name);
    UUID messageIdTwo = UUID.randomUUID();
    mention.addMessageId(messageIdTwo);
    
    Assert.assertEquals(name, mention.getName());
    assert(mention.getMessageIds().contains(messageIdOne));
    assert(mention.getMessageIds().contains(messageIdTwo));
  }
}