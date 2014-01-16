<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://sandag.com/commandcenter/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
  <div class="row">
    <div class="col-md-12">
      <h3>Log files for scenario '<a href="<c:url value="/jobs/${job.status == RUNNING ? 'running' : 'finished'}" />?highlight=${job.id}">${job.scenario}</a>'</h3>
      <table class="table table-striped">
        <tbody>
          <c:forEach items="${logs}" var="log">
            <tr>
              <td><a href="<c:url value="/log/job/${job.id}?logPath=${fn:urlEncode(log)}" />">${log}</a></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>
