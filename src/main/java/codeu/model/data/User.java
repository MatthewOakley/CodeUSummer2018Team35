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
import com.google.appengine.api.datastore.Text;

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final String passwordHash;
  private final Instant creation;
  private final boolean isAdmin;
  private String aboutMe;
  private Text profilePic;

  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param passwordHash the password hash of this User
   * @param creation the creation time of this User
   * @param aboutMe the aboutMe of this User
   * @param admin if the User is an admin or not
   */
  public User(UUID id, String name, String passwordHash, Instant creation, String aboutMe, boolean isAdmin, Text profilePic) {
    this.id = id;
    this.name = name;
    this.passwordHash = passwordHash;
    this.creation = creation;
    this.aboutMe = aboutMe;
    this.isAdmin = isAdmin;
    this.profilePic = profilePic;
  }

  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }

  /** Returns the password hash of this User. */
  public String getPasswordHash() {
    return passwordHash;
  }

  /** Returns the creation time of this User. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns true if the user is an admin. */
  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public String getAboutMe() {
    return aboutMe;
  }

  public Text getProfilePic() {
	  return profilePic;
  }

  public void setProfilePic(Text profilePic) {
	  this.profilePic = profilePic;
  }
}
