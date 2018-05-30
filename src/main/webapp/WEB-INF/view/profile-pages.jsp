<!DOCTYPE html>
<html>
<head>
  <title>Profile Pages</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/users/<%= request.getSession().getAttribute("user") %>">My Profile</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
  </nav>

  <!--TODO(asma526):

  Add an about me box that can only be edited by the user
  Add a box that displays all messages sent out by the user

  -->

  <h1>Profile Pages</h1>
</body>
</html>
