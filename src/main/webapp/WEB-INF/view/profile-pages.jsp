<%@ page import="codeu.model.store.basic.UserStore" %>

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
  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <% if(request.getSession().getAttribute("user") != null){ %>
      <h1><%= request.getSession().getAttribute("user") %>'s Profile Page</h1>

      <hr/>

      <h2>About <%= request.getSession().getAttribute("user") %> </h2>
      <p> <%= UserStore.getInstance().getUser((String)request.getSession().getAttribute("user")).getAboutMe() %> </p>

      <h2>Edit About Me</h2>

      <form action="/users/<%= request.getSession().getAttribute("user") %>" method="POST">

        <div class="form-group">
          <input type="text" name="aboutMe">
        </div>

        <br/>
        
        <button type="submit">Submit</button>
      </form>

      <hr/>
    <% } %>

  </div>
</body>
</html>
