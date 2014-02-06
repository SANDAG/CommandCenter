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
		$('.deleteButton').click(function() {
			deleteJob($(this));
		});

		$('.upButton').click(function() {
			moveJob($(this), true);
		});

		$('.downButton').click(function() {
			moveJob($(this), false);
		});
	});

	function deleteJob(button) {
		var row = button.closest('tr');
		var id = row.data('job_id');
		var successFunction = function(response) {
			row.remove();
			$('#message').text(response);
		};
		var errorFunction = function(e) {
			alert('There was an error deleting your job; please try again later.');
		};

		$.ajax({
			type : 'DELETE',
			url : '<c:url value="/job/"/>' + id,
			success : successFunction,
			error : errorFunction
		});
	}

	function moveJob(button, isMoveUp) {
		var row = button.closest('tr');
		var id = row.data('job_id');
		var completeFunction = function() {
			var href = highlightJobId ? window.location.href.replace(
					'highlight=' + highlightJobId, 'highlight=' + id)
					: window.location.href
							+ (window.location.search.length ? '&' : '?')
							+ 'highlight=' + id;
			window.location.assign(href);
		};

		$.ajax({
			type : 'GET',
			async : false,
			url : '<c:url value="/jobs/queued/" />' + id + '/move?moveUp='
					+ isMoveUp,
			complete : completeFunction
		});
	}
</script>
</head>
<body>
  <div class="row">
    <div class="col-md-8">
      <%@ include file="jobsNavigation.jspf"%>
    </div>
    <div class="col-md-4">
      <a class="btn btn-success pull-right" href="<c:url value="/job" />"><i class="glyphicon glyphicon-plus"></i>
        Add Job</a>
    </div>
  </div>
  <div class="row">
    <div class="col-md-12">
      <h3 id="message" class="pull-left"></h3>
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
            <th>Created</th>
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
              <td>${job.created}</td>
              <td><c:if test="${owned && moveUpIds.contains(job.id)}">
                  <button type="button" class="btn btn-default btn-small upButton" title="Move up the queue">
                    <span class="glyphicon glyphicon-arrow-up"></span>
                  </button>
                </c:if></td>
              <td><c:if test="${owned && moveDownIds.contains(job.id)}">
                  <button type="button" class="btn btn-default btn-small downButton" title="Move down the queue">
                    <span class="glyphicon glyphicon-arrow-down"></span>
                  </button>
                </c:if></td>
              <td><c:if test="${owned}">
                  <button type="button" class="btn btn-danger btn-small deleteButton" title="Remove from queue">
                    <span class="glyphicon glyphicon-trash"></span>
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
