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
// limitations under the License.package codeu.model.data;
package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;


/** Class representing a mention. */
public class Mention {

  private Set<UUID> messageIds;
  private final String mentionedUser;

  /**
   * Constructs a new Mention.
   *
   * @param id the ID of conversation
   * @param string of mentioned user
   */
  public Mention(UUID messageId, String mentionedUser) {
    this.messageIds = new HashSet<UUID>(); 
    this.messageIds.add(messageId);
    this.mentionedUser = mentionedUser;
  }

  public Mention(Set<UUID> messageIds, String mentionedUser) {
    this.messageIds = messageIds; 
    this.mentionedUser = mentionedUser;
  }

  public void addMessageId(UUID id) {
    messageIds.add(id);
  }

  /** Returns the ID of the Conversation this Message belongs to. */
  public Set<UUID> getMessageIds() {
    return messageIds;
  }

 /** Returns mentioned user. */
  public String getName() {
    return mentionedUser;
  }

}
