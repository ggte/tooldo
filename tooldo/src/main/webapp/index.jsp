<%
String host = request.getHeader("host");
response.sendRedirect(request.getContextPath() + "/pages/secure/main.jsf");
%>
