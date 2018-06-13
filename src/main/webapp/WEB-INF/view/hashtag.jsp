<!-- 
  hashtag.jsp
-->
<!DOCTYPE html>
<html>
<head>
  <title>Hashtag | <%= request.getAttribute("hashtag") %></title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>
  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
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
    Welcome to the hashtag page!
  </p>
  <!-- The stats about the web app -->
  <p> Your hashtag is <%= request.getAttribute("hashtag") %></p>
  <!-- 
  TO-DO(Matthew Oakley) I need to get the entire hashtag the user wants
  then display all corresponding messages here -->
</body>
</html>