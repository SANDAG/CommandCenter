<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head></head>
<body>
  <div class="row">
    <div class="col-md-12">
      <h3>${message}</h3>
      <table class="table table-striped">
        <thead>
          <tr>
            <th>Model</th>
            <th>Scenario name</th>
            <th>Study name</th>
            <th>Scenario Location</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${jobs}" var="job">
            <td>${job.model}</td>
            <td>${job.scenario}</td>
            <td>${job.study}</td>
            <td>${job.scenarioLocation}</td>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>
