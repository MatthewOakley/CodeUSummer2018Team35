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
    <p style="text-align: center;"> Welcome to the admin page! </p>
    <!-- The stats about the web app -->
    <p style="text-align: center;"> Stats about the web app </p>
    <p style="text-align: center;"><strong>Users:</strong> <%= request.getAttribute("userCount") %></p>
    <p style="text-align: center;"><strong>Conversations:</strong> <%= request.getAttribute("conversationCount") %></p>
    <p style="text-align: center;"><strong>Messages:</strong> <%= request.getAttribute("messageCount") %></p>
    <p style="text-align: center;"><strong>Newest User:</strong> <%= request.getAttribute("newestUser") %></p>
    <p style="text-align: center;"><strong>Number of Attacks:</strong> <%= request.getAttribute("attackCount") %></p>
</body>
</html>
