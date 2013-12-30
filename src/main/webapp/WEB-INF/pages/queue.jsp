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

		$('.deleteButton').click(function() {
			deleteJob($(this));
		});

		$('#user-toggle').on('switch-change', function(e, data) {
			data.value ? $(".unowned").show(500) : $(".unowned").hide(500);
		});
	});

	function deleteJob(button) {
		var row = button.closest('tr'), id = row.data('job_id');

		// sorry - the Eclipse formatter does this
		$
				.ajax({
					type : 'DELETE',
					url : 'job/' + id,
					success : function(response) {
						row.remove();
						$('#message').text(response);
					},
					error : function(e) {
						alert('There was an error deleting your job; please try again later.');
					}
				});
	}
</script>
<style>
.table-striped>tbody>tr:nth-child(odd).owned>td {
	background-color: #ff9;
}

.table-striped>tbody>tr:nth-child(even).owned>td {
	background-color: #ffc;
}

.owned-marker,#user-toggle .glyphicon {
	color: #ccc;
}

#user-toggle label.switch-small {
	padding-top: 0;
}

.owned .owned-marker {
	color: #000;
}
</style>
</head>
<body>
  <div class="row">
    <div class="col-md-12">
      <h3 id="message" class="pull-left">${message}</h3>
      <div id="user-toggle" class="make-switch switch-small pull-right" data-label-icon="glyphicon glyphicon-user"
        data-on-label="Shown" data-off-label="Hidden">
        <input type="checkbox" checked>
      </div>
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
            <c:set var="owned" value="${jobAccessManager.canDelete(job, principal)}" />
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
