<!--
  Admin.jsp
-->
<!DOCTYPE html>
<html>
<head>
  <title>Admin</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>
  <!-- This is redirecting the user if they are not an admin -->
  <%  if (request.getSession().getAttribute("user") == null ||
            !(boolean)request.getSession().getAttribute("adminStatus")) {
        String redirectURL = "http://codeusummer2018team35.appspot.com";
        response.sendRedirect(redirectURL);
      }
  %>
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
  <!-- to see if I made it to the right page -->
  <p>
    Welcome to the admin page!
  </p>
  <!-- The stats about the web app -->
  <p> Stats about the web app </p>
  <ul id="stats">
    <li>Users: <%= request.getAttribute("userCount") %></li>
    <li>Conversations: <%= request.getAttribute("conversationCount") %></li>
    <li>Messages: <%= request.getAttribute("messageCount") %></li>
    <li>Newest User: <%= request.getAttribute("newestUser") %></li>
    <li>Number of Attacks: <%= request.getAttribute("attackCount") %></li>
  </ul>
</body>
</html>
