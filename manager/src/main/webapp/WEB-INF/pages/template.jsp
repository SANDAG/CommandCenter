<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<!DOCTYPE html>
<html>
<head>
<title><decorator:title default="SANDAG Command Center" /></title>

<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">
<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-glyphicons.css" rel="stylesheet">
<link href="<c:url value="/css/styles.css" />" rel="stylesheet">

<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script>
	function getParameterByName(name) {
		var match = RegExp('[?&]' + name + '=([^&]*)').exec(
				window.location.search);
		return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
	}
</script>
<decorator:head />
</head>
<body>
  <div class="navbar navbar-default navbar-static-top">
    <div class="container">
      <div class="navbar-header">
        <a class="navbar-brand" href="<c:url value="/jobs/queued" />">SANDAG Command Center</a>
      </div>
    </div>
  </div>
  <div class="container">
    <decorator:body />
  </div>
</body>
</html>