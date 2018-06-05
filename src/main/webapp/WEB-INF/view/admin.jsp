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
  <%  if(request.getSession().getAttribute("user") == null) {
        String redirectURL = "http://localhost:8080/";
        response.sendRedirect(redirectURL);
      } else if((boolean)request.getSession().getAttribute("adminStatus") == false) {
        String redirectURL = "http://localhost:8080/";
        response.sendRedirect(redirectURL);
      } %>
  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null) { %>
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
  </ul>
</body>
</html>