<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
  <script>
  	$(function () {
  		appendLog();
  	});
  	
  	function appendLog() {
		var startByte = 0;

  		var fetch = function () {
  	  		var errorFunction = function () {
  	  			alert("There was a problem retrieving the log; please refresh to try again.");
  	  		};
  			var successFunction = function (data) {
  				$('#logContents').append(data);
  				startByte += data.length;
  				if (data.length > 0) {
  					fetch();
  				} else {
  					// more data may be written... pause a bit before trying again
  					setTimeout(fetch, 3000);
  				}
  			};
    		$.ajax({
    			url : '${url}&startByte=' + startByte,
    			dataType: 'text',
    			success : successFunction,
    			error : errorFunction
    		});
  		};
  		fetch();
  	}
  </script>
  <style>
    #logContents {
      white-space: pre;
    }
  </style>
</head>
<body>
  <div class="row">
    <div class="col-md-12">
      <button type="button" class="btn btn-info" onclick="window.history.back()"><i class="glyphicon glyphicon-circle-arrow-left"></i> Back</button> 
      <h3>Scenario <strong><a href="<c:url value="/jobs/${job.status == RUNNING ? 'running' : 'finished'}" />?highlight=${job.id}">${job.scenario}</a></strong>&#39;s log file <strong>${path}</strong></h3>
      <div id="logContents"></div>
    </div>
  </div>
</body>
</html>
