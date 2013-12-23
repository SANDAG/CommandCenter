<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<sec:authentication var="principal" property="principal" />
<!DOCTYPE html>
<html>
<head>
  <script>
  	$(function() {
  		setActiveNav('nav-queue');
  		
  		$('.deleteButton').click(function () {
  			deleteJob($(this));
  		});
  	});
  	
	function deleteJob(button) {
      var id = button.data('job_id'),
      	row = button.closest('tr');
      	
      $.ajax({
        type: 'DELETE',
        url: 'job/' + id,
        success: function (response) {
          row.remove();
		  $('#message').text(response);
        },
        error: function (e) {
          alert('There was an error deleting your job; please try again later.');
        }
	  });  	
  	}
  </script>
</head>
<body>
  <div class="row">
    <div class="col-md-12">
      <h3 id="message">${message}</h3>
      <table class="table table-striped">
        <thead>
          <tr>
            <th>Model</th>
            <th>Scenario name</th>
            <th>Study name</th>
            <th>Scenario Location</th>
            <th>Created by</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${jobs}" var="job">
            <tr>
              <td>${job.model}</td>
              <td>${job.scenario}</td>
              <td>${job.study}</td>
              <td>${job.scenarioLocation}</td>
              <td>${job.user.principal}</td>
              <td>
                <sec:authorize access="${jobAccessManager.canDelete(job, principal)}">
                  <button type="button" class="btn btn-danger btn-xs deleteButton" data-job_id="${job.id}" title="Remove from queue"><span class="glyphicon glyphicon-trash"></span>&nbsp;</button>
                </sec:authorize>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>
