<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<sec:authentication var="principal" property="principal" />
<!DOCTYPE html>
<html>
<head>
<script>
	var highlightJobId;
	$(function() {
		highlightJobId = getParameterByName('highlight');

		setActiveNav('nav-queue');

		$('.deleteButton').click(function() {
			deleteJob($(this));
		});

		$('.upButton').click(function() {
			moveJob($(this), true);
		});

		$('.downButton').click(function() {
			moveJob($(this), false);
		});

		$('#user-toggle').on('switch-change', function(e, data) {
			data.value ? $(".unowned").show(500) : $(".unowned").hide(500);
		});

		$('tr[data-job_id=' + highlightJobId + ']').addClass('highlight');
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
			url : 'job/' + id,
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
			url : 'jobs/' + id + '/move?moveUp=' + isMoveUp,
			complete : completeFunction
		});
	}
</script>
</head>
<body>
  <div class="row">
    <div class="col-md-6">
      <%@ include file="jobsNavigation.jsp" %>
    </div>
    <div class="col-md-6">
      <div id="user-toggle" class="make-switch switch-small pull-right" data-label-icon="glyphicon glyphicon-user"
        data-on-label="Shown" data-off-label="Hidden">
        <input type="checkbox" checked>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-md-12">
      <h3 id="message" class="pull-left">${message}</h3>
      <table class="table table-striped">
        <thead>
          <tr>
            <th></th>
            <th></th>
            <th>Model</th>
            <th>Scenario name</th>
            <th>Study name</th>
            <th>Scenario Location</th>
            <th>Created by</th>
            <th colspan="99"></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${jobs}" var="job" varStatus="status">
            <c:set var="owned" value="${jobAccessManager.canUpdate(job, principal)}" />
            <tr class="${owned ? 'owned' : 'unowned'}" data-job_id="${job.id}">
              <td><span class="owned-marker glyphicon glyphicon-user"></span></td>
              <td class="text-right">${status.count}.</td>
              <td>${job.model}</td>
              <td>${job.scenario}</td>
              <td>${job.study}</td>
              <td>${job.scenarioLocation}</td>
              <td>${job.user.principal}</td>
              <td><c:if test="${owned && moveUpIds.contains(job.id)}">
                  <button type="button" class="btn btn-default btn-xs upButton" title="Move up the queue">
                    <span class="glyphicon glyphicon-arrow-up"></span>
                  </button>
                </c:if></td>
              <td><c:if test="${owned && moveDownIds.contains(job.id)}">
                  <button type="button" class="btn btn-default btn-xs downButton" title="Move down the queue">
                    <span class="glyphicon glyphicon-arrow-down"></span>
                  </button>
                </c:if></td>
              <td><c:if test="${owned}">
                  <button type="button" class="btn btn-danger btn-xs deleteButton" title="Remove from queue">
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
