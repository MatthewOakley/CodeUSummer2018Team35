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


<%@ page import="java.util.List" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.Mention" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title><%= conversation.getTitle() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">

  <style>
    #chat {
      background-color: white;
      height: 500px;
      overflow-y: scroll
    }

  </style>

  <script>
    // scroll the chat div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };
  </script>
</head>
<body onload="scrollChat()">
  <nav>
    <a id="navTitle" href="/">Pied Piper Chat App</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a href="/users/<%= request.getSession().getAttribute("user") %>">My Profile</a>
    <% } %>
    <a href="/conversations">Conversations</a>
      <% if (request.getSession().getAttribute("user") != null) { %>
    <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else { %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <div id="container">

    <h1><%= conversation.getTitle() %>
      <a href="" style="float: right">&#8635;</a></h1>

    <hr/>

    <div id="chat">
      <ul>
    <%
      for (Message message : messages) {
        String author = UserStore.getInstance().getUser(message.getAuthorId()).getName();
    %>
        <%
        String content = message.getContent();
        String[] messageSplit = content.split(" ");
        String output = "";
        for (String word : messageSplit) {
          if (word.length() >= 2 &&  word.charAt(0) == '#') {
            if(word.charAt(word.length() - 1) == '.') {
              word = word.substring(0, word.length() - 1);
            }
            word = "<a href='../../hashtag/" + word.substring(1) + "'>" 
              + word + "</a>";
          }
          output = output + " " + word;
        }
        %>


        <%
        if (message.getType().equals("image")){
        %>
            <li><strong><%= author %>:</strong> 
            <img src="<%= message.getContent() %>" alt = "Image" width = 50% height = 50%> </li>
        <%
        } else if (message.getType().equals("text") || message.getType() == null) {
        %>
          <li><strong><%= author %>:</strong> <%= output %>
  
      <% } %>

        <% if (UserStore.getInstance().getUser(author).getName().equals(
                request.getSession().getAttribute("user"))) { %>
          <form action="/chat/<%= conversation.getTitle() %>" method="POST">
            <button type="submit">Edit</button>
            <input type="text" name="edit">
            <input type="hidden" name="messageId" value="<%= message.getId() %>">
          </form>
        <% } %>
        <% if (request.getSession().getAttribute("user") != null) { %>
          <form action="/chat/<%= conversation.getTitle() %>" method="POST">
            <button type="submit">Reply</button>
            <input type="text" name="message">
            <input type="hidden" name="messageId" value="<%= message.getId() %>">
            <input type="hidden" name="reply" value="true">
          </form>
        <% } %>
        <% if (UserStore.getInstance().getUser(author).getName().equals(request.getSession().getAttribute("user"))) { %>

          <form action="/chat/<%= conversation.getTitle() %>" method="POST">
            <button type="submit">Delete</button>
            <input type="hidden" name="delete" value="true">
            <input type="hidden" name="messageId" value="<%= message.getId() %>">
          </form>
        <% } %>
      </li>
        <ul class="tab">
      <%
        for (Message reply : message.getReplies()) {
        
        author = UserStore.getInstance().getUser(reply.getAuthorId()).getName();
        String contentReply = reply.getContent();
        String[] replySplit = contentReply.split(" ");
        String replyOutput = "";
        for (String word : replySplit) {
          if (word.length() >= 2 && word.charAt(0) == '#') {
            if(word.charAt(word.length() - 1) == '.') {
              word = word.substring(0, word.length() - 1);
            } 
            word = "<a href='../../hashtag/" + word.substring(1) + "'>" 
              + word + "</a>";
          }
          replyOutput = replyOutput + " " + word;
        }
      %>
        <li>
            <strong><%= author %>:</strong> <%= replyOutput %>
            <% if (UserStore.getInstance().getUser(author).getName().equals(
                    request.getSession().getAttribute("user"))) { %>
              <form action="/chat/<%= conversation.getTitle() %>" method="POST">
                <button type="submit">Edit</button>
                <input type="text" name="edit">
                <input type="hidden" name="messageId" value="<%= message.getId() %>">
              </form>
            <% } %>
        </li>
      <%
        }
      %>
        </ul>
    <%
      }
    %>
      </ul>
    </div>

    <hr/>

    <% if (request.getSession().getAttribute("user") != null) { %>
    <form action="/chat/<%= conversation.getTitle() %>" method="POST">
        <input type="text" name="message">
        <br/>
        <input type="hidden" name="reply" value="false">
        <button type="submit">Send</button>
    </form>

    <form action="<%= blobstoreService.createUploadUrl("/ImageUploadServlet") %>" method= "POST" enctype = "multipart/form-data">
            <input type="hidden" name="conversationTitle" value="<%=conversation.getTitle() %>"> 
            <input type="file" name="myFile" >
            <input type="submit" value="Submit">
    </form>

    <% } else { %>
      <p><a href="/login">Login</a> to send a message.</p>
    <% } %>
    
    <hr/>
  </div>
</body>
</html>
