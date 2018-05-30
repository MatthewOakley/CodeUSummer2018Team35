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
    <!-- 
      TO-DO(Matthew Oakley) I am going to add checking the current stats
      I just have this basic setup to get the users information and will
      carry it over to the other stats
    -->
    <li>Users: <span id="users"><%= request.getAttribute("userCount") %></span></li>
    <li>Conversations: <span id="conversations"><%= 
        request.getAttribute("conversationCount") %></span></li>
    <li>Messages: <span id="messages"><%= 
        request.getAttribute("messageCount") %></span></li>
    <li>Most active user: <span id="mostActive"></span></li>
    <li>Newest User: <span id="newestUser"></span></li>
    <li>Wordiest user: <span id="wordiestUser"></span></li>
  </ul>
</body>
</html>