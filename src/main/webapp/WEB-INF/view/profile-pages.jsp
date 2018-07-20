<%@ page import="java.util.List" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.MentionStore" %>
<%@ page import="codeu.model.data.Mention" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="com.google.appengine.api.datastore.Text" %>

<%
/** Gets the UserStore instance to access all users. */
UserStore userStore = UserStore.getInstance();
%>
<% List<Message> messages = (List<Message>) request.getAttribute("messages"); %>

<!DOCTYPE html>
<html>
<head>
  <title>Profile Pages</title>
  <link rel="stylesheet" href="/css/main.css">

  <style>
    #profileMessages {
      background-color: white;
      height: 200px;
      overflow-y: scroll
    }
    #mentionedMessages {
      background-color: white;
      height: 200px;
      overflow-y: scroll
    }

  </style>
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

  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } else {%>
      <% if(request.getSession().getAttribute("user") != null){ %>

        <h1><%= request.getAttribute("username") %>'s Profile Page</h1>

        <hr/>

        <%
          String userProfile = (String) request.getAttribute("username");
          User currentUser = userStore.getUser(userProfile);
        %>

        <% if (currentUser.getProfilePic() == null) { %>
          <img src="../images/default_pfp.jpg" alt="temp"/>
        <% } else { %>
          <img src="data:image/jpeg;base64,<%= currentUser.getProfilePic().getValue() %>" alt="temp" width="250" />
        <% } %>

        <% if (request.getSession().getAttribute("user").equals(request.getAttribute("username"))) { %>
           <form action="/users/<%= request.getSession().getAttribute("user") %>" method="POST" enctype="multipart/form-data">
             <label for="EditProfilePicture">Edit Your Profile Picture: </label>
             <br/>
             <br/>
             <input type="file" name="pic" accept="image/*">
             <button type="submit" name="EditProfilePage" value="EditProfilePicture" class="btn btn-primary">Upload</button>
           </form>
        <% } else {} %>

        <hr/>

        <h2>About <%= request.getAttribute("username") %> </h2>
        <p> <%= UserStore.getInstance().getUser((String)request.getAttribute("username")).getAboutMe() %> </p>

        <% if(request.getSession().getAttribute("user").equals(request.getAttribute("username"))){ %>
          <h2>Edit About Me</h2>

          <form action="/users/<%= request.getSession().getAttribute("user") %>" method="POST">

            <div class="form-group">
              <input type="text" name="aboutMe">
            </div>

            <br/>

            <button type="submit">Submit</button>
          </form>
        <% } %>

        <hr/>

        <h2><%= request.getAttribute("username") %>'s Sent Messages</h2>
        <div id="profileMessages">
          <ul>
            <% for (Message message : messages) {
                String author = UserStore.getInstance().getUser(message.getAuthorId()).getName();
            %>
                <li><strong><%= author %>:</strong> <%=message.getStyledContent(message.getContent()) %></li>
            <% } %>
          </ul>
        </div>

        <h2><%= request.getAttribute("username") %>'s Mentioned Messages</h2>
        <div id="mentionedMessages">
          <ul>
            <% for (Message message : messages) { %>
                <li><%= message.getStyledContent(message.getContent()) %></li>

            <% } %>
          </ul>
        </div>
      <% } %>
    <% } %>
    <br/>
  </div>
</body>
</html>
