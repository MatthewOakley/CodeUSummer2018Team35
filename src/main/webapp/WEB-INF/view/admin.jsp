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
  <!-- 
    TO-DO(Matthew Oakley) I am going to add the stats for the admin page
    I just have this basic setup to see if I could get the servlets working
  -->
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
    Admin Page!!!
  </p>
</body>
</html>