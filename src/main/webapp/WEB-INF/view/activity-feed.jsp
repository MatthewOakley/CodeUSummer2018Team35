<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

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
        <%
        List<Conversation> conversations =
            (List<Conversation>) request.getAttribute("conversations");

        if(!(conversations == null || conversations.isEmpty())) {
        %>
            <ul>
        <%
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
            }
        %>
            </ul>
        <%
        }
        %>

        </div>
    </div>

</body>
</html>
