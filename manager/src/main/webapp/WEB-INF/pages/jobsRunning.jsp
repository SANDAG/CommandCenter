<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<sec:authentication var="principal" property="principal" />
<!DOCTYPE html>
<html>
<head>
<%@ include file="jobsCommonScripts.jspf"%>
<script>
	$(function() {
		$('.cancelButton').click(function() {
			cancelJob($(this));
		});
	});

	function cancelJob(button) {
		var row = button.closest('tr');
		var id = row.data('job_id');
		var successFunction = function(response) {
			row.remove();
			$('#message').text(response);
		};
		var errorFunction = function(e) {
			alert('There was an error cancelling your job; please try again later.');
		};

		$.ajax({
			type : 'PUT',
			url : '<c:url value="/job/cancel/"/>' + id,
			success : successFunction,
			error : errorFunction
		});
	}
</script>
</head>
<body>
  <div class="row">
    <div class="col-md-8">
      <%@ include file="jobsNavigation.jspf"%>
    </div>
    <div class="col-md-4"></div>
  </div>
  <div class="row">
    <div class="col-md-12">
      <c:set var="admin" value="${roleChecker.isAdmin(pageContext.request)}" />
      <table class="table table-striped">
        <thead>
          <tr>
            <c:if test="not admin">
              <th class="toggle"><i class="glyphicon glyphicon-user unowned"></i>&nbsp;<input type="checkbox"
                id="user-toggle" checked="checked" /></th>
            </c:if>
            <th></th>
            <th>Model</th>
            <th>Scenario name</th>
            <th>Study name</th>
            <th>Scenario location</th>
            <th>Created by</th>
            <th>Runner</th>
            <th>Started</th>
            <th colspan="99"></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${jobs}" var="job" varStatus="status">
            <c:set var="owned" value="${jobAccessManager.canUpdate(pageContext.request, job, principal)}" />
            <tr class="${owned ? 'owned' : 'unowned'}" data-job_id="${job.id}">
              <c:if test="not admin">
                <td><span class="owned-marker glyphicon glyphicon-user"></span></td>
              </c:if>
              <td class="text-right">${status.count}.</td>
              <td>${job.model}</td>
              <td>${job.scenario}</td>
              <td>${job.study}</td>
              <td>${job.scenarioLocation}</td>
              <td>${job.user.principal}</td>
              <td>${job.runner}</td>
              <td>${job.started}</td>
              <td><c:if test="${owned}">
                  <a href="<c:url value="/logs/job/${job.id}" />" class="btn btn-info btn-small" title="View logs"> <span
                    class="glyphicon glyphicon-folder-open"></span>
                  </a>
                </c:if></td>
              <td><c:if test="${owned}">
                  <button type="button" class="btn btn-danger btn-small cancelButton" title="Cancel running job">
                    <span class="glyphicon glyphicon-stop"></span>
                  </button>
                </c:if></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>
