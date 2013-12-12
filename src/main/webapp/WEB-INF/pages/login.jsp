<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <script>
  	// checking jquery integration
  	$(function () {$("#content").addClass("well")});
  </script>
</head>

<body>
  <div class="row">
    <div class="col-md-3">
    </div>
    <div id="content" class="col-md-9">
      <h3>Message : ${message}</h3>
    </div>
  </div>
		
</body>
</html>