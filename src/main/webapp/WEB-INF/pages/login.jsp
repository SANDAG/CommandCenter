<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script>
	// checking jquery integration
	$(function() {
		$("#content").addClass("well")
	});
</script>
</head>

<body>
  <div class="row">
    <div class="col-md-3"></div>
    <div id="content" class="col-md-9">
      <h3>Message : ${message}</h3>

      <%
      	if (request.getUserPrincipal() != null) {
      %>
      
      waffle.spring.WindowsAuthenticationToken).
      
      Your user principal name is <b><%=request.getUserPrincipal().getName()%></b> 
      
      All roles (including AD groups):<br>
      <%=((waffle.spring.WindowsAuthenticationToken)(request.getUserPrincipal())).getAuthorities()%><br>
      <br>

      <%
      	if (request.isUserInRole("ROLE_I-RSG\\DEVELOPERS")) {
      %>
      You are an RSG dev. <br>
      <%
      	} else {
      %>
      Not an RSG dev.<br>
      <%
      	}
      	} else {
      %>
      No user principal could be identified. <br> <br>
      <%
      	}
      %>
    </div>
  </div>

</body>
</html>
