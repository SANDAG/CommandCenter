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
	$('.unarchiveButton').click(function() {
		unarchiveJob($(this));
	});
});

function unarchiveJob(button) {
	var row = button.closest('tr');
	var id = row.data('job_id');
	var successFunction = function(response) {
		row.remove();
		$('#message').text(response);
	};
	var errorFunction = function(e) {
		alert('There was an error unarchiving your job; please try again later.');
	};

	$.ajax({
		type : 'GET',
		async : true,
		url : '<c:url value="/admin/job/unarchive/"/>' + id,
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
      <h3 id="message" class="pull-left"></h3>
      <c:set var="admin" value="${roleChecker.isAdmin(pageContext.request)}" />
      <table class="table table-striped">
        <thead>
          <tr>
            <c:if test="not admin">
              <th class="toggle"><i class="glyphicon glyphicon-user unowned"></i>&nbsp;<input type="checkbox"
                id="user-toggle" checked="checked" /></th>
            </c:if>
            <th>Model</th>
            <th>Scenario name</th>
            <th>Study name</th>
            <th>Created by</th>
            <th colspan="99"></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${jobs}" var="job" varStatus="status">
            <c:set var="owned" value="${jobAccessManager.canUpdate(pageContext.request, job, principal)}" />
            <tr class="${owned ? 'owned' : 'unowned'} ${failed ? 'failed' : 'finished'}" data-job_id="${job.id}">
              <c:if test="not admin">
                <td><span class="owned-marker glyphicon glyphicon-user"></span></td>
              </c:if>
              <td>${job.model}</td>
              <td>${job.scenario}</td>
              <td>${job.study}</td>
              <td>${job.user.principal}</td>
              <c:if test="${admin}">
                <td><button type="button" class="btn btn-success btn-small unarchiveButton" title="Unarchive">
                    <span class="glyphicon glyphicon-floppy-open"></span>
                </button></td>
              </c:if>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>
