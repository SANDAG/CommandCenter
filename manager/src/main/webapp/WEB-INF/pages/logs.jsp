<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
  <div class="row">
    <div class="col-md-12">
      <h3 id="message" class="pull-left">${message}</h3>
      <table class="table table-striped">
        <thead>
          <tr>
            <th>Log files for scenario '<a href="<c:url value="/jobs/${job.status == RUNNING ? 'running' : 'complete'}" />">${job.scenario}</a>'</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${logs}" var="log">
            <tr>
              <td>${log}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>
