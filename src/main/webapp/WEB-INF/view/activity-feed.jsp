<%@ page import="java.util.List" %>
<%@ page import="java.util.UUID" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.MessageStore" %>

<!DOCTYPE html>
<html>
<head>
    <title>Activity Feed</title>
    <link rel="stylesheet" href="/css/main.css">
</head>
<body>

<!--TODO(StevenAb):

 Need to get all login, conversation, and message data
 and display it in chronological order.

-->

    <nav>
        <a id="navTitle" href="/">CodeU Chat App</a>
        <% if(request.getSession().getAttribute("user") != null){ %>
          <a href="/users/<%= request.getSession().getAttribute("user") %>">My Profile</a>
        <% } %>
        <a href="/activityfeed">Activity Feed</a>
        <a href="/conversations">Conversations</a>
        <% if(request.getSession().getAttribute("user") != null){ %>
            <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
        <% } else{ %>
            <a href="/login">Login</a>
        <% } %>
        <a href="/about.jsp">About</a>
    </nav>

    <div id="container">

        <h1>Welcome to the activity feed.</h1>

        <div id = "container">
        <ul>
        <%
        List<Conversation> conversations =
          (List<Conversation>) request.getAttribute("conversations");

        for(Conversation conversation : conversations) {
        %>
            <li>
              <strong><%= conversation.getCreationTime() %>:</strong>
              <%= UserStore.getInstance().getUser(
                conversation.getOwnerId()).getName() %> created conversation
              <a href="/chat/<%= conversation.getTitle() %>">
                <%= conversation.getTitle() %>
              </a>
            </li>
        <%
            List<Message> messages = (List<Message>) MessageStore.getInstance()
              .getMessagesInConversation(conversation.getId());

            if(messages != null && !messages.isEmpty()) {
                for(Message message : messages) {
        %>
                    <li>
                        <strong><%= message.getCreationTime() %>:</strong>
                        <%= UserStore.getInstance().getUser(
                          message.getAuthorId()).getName() %> sent a message in
                        <a href="/chat/<%= conversation.getTitle() %>">
                          <%= conversation.getTitle() %>
                        </a>: "
                        <%= message.getContent() %>"
                    </li>
        <%
                }
            }
        }

        List<UUID> userIds = (List<UUID>) UserStore.getInstance().getUserIds();

        for(UUID id : userIds) {
            User user = UserStore.getInstance().getUser(id);
        %>
            <li>
                <strong><%= user.getCreationTime() %>:</strong>
                <%= user.getName() %> joined!
            </li>
        <%
        }
        %>

        </ul>
        </div>
    </div>

</body>
</html>
