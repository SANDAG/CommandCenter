<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<sec:authentication var="principal" property="principal" />
<!DOCTYPE html>
<html>
<head>
<%@ include file="jobsCommonScripts.jspf"%>
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
      <table class="table table-striped">
        <thead>
          <tr>
            <th><i class="glyphicon glyphicon-user unowned"></i>&nbsp;&nbsp;<input type="checkbox"
              id="user-toggle" checked="checked" /></th>
            <th></th>
            <th>Model</th>
            <th>Scenario name</th>
            <th>Study name</th>
            <th>Scenario location</th>
            <th>Created by</th>
            <th>Runner</th>
            <th>Started</th>
            <th></th>
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
              <td>${job.runner}</td>
              <td>${job.started}</td>
              <td><c:if test="${owned}">
                  <a href="<c:url value="/logs/job/${job.id}" />" class="btn btn-info btn-xs" title="View logs"> <span
                    class="glyphicon glyphicon-folder-open"></span>
                  </a>
                </c:if></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>
