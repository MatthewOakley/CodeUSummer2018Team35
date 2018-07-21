<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<!DOCTYPE html>
<html>
<head>
  <title>Pied Piper Chat App</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">Pied Piper Chat App</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a href="/users/<%= request.getSession().getAttribute("user") %>">My Profile</a>
    <% } %>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1>About Pied Piper's Chat App</h1>
      <p>
        This is how far we have gotten with our chat app!!!
      </p>

      <ul>
        <li><strong>Admin:</strong> To access this you must make a user called "admin" and password that is "admin". Then go to /admin part of the webpage. This will show the stats of the webpage and amount of attacks that have been detected.</li>
        <li><strong>Hashtag:</strong> Putting a # before a word in conversations will create a link that can be clicked. Click and all messages with that hashtag will be displayed.</li>
        <li><strong>SQL-Injection:</strong> When making a new user or signing in it will detect if there is a " ; ", " ' " or " " " in the username. If it detects an "attack" it will log the user's client info and current time and send it to a log. The total amount of attacks can be seen on the admin page.</li>
        <li><strong>Profile Page:</strong>To access the profile page of any user go to /users/username , username being the person's profile you would like to access. You can also easily access your own profile by clicking on the My Profile tab which only appears after the user logs in. 
	On the profile page a user can add/edit their bio or profile pic. The profile page also displays all of the users sent messages and mentioned messages.
</li>
        <li><strong>Delete Button:</strong>A user can delete any message or conversation by clicking the delete button.</li>
        <li><strong>Emojis and Styled Text:</strong>Type in the desired emoji with a colon on both ends and emoji will be displayed when message is sent. Ex. :camel: (find emoji guide at <a href ="https://github.com/vdurmont/emoji-java">https://github.com/vdurmont/emoji-java</a> To style text do the following ---italics: asterisk or underscore on both end ---- bold: double asterisks or double underscores on both ends ---italics and bold: double asterisks followed by single underscore on both ends.</li>
      </ul>

      <p>
        Enjoy the work we have put into this website!
      </p>
    </div>
  </div>
</body>
</html>
