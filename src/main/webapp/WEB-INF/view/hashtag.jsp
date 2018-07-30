<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Message" %>
<!DOCTYPE html>
<html>
<head>
  <title>Hashtag | <%= request.getAttribute("hashtagName") %></title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>
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
  <p style="text-align: center;"> Welcome to the hashtag page! </p>
  <!-- The current hashtag the user is on -->
  <p style="text-align: center;"> Your hashtag is <strong><%= request.getAttribute("hashtagName") %></strong></p>
  <p style="text-align: center;"> <strong>Messages:</strong> </p>
  <% List<Message> messages = (List<Message>)request.getAttribute("messages");
    for (Message message : messages) { %>
    <p style="text-align: center;">
    <%= message.getContent() %>
    </p>
  <% } %>
  </body>
</html>
