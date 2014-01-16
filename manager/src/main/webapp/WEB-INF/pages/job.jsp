<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/"%>
<!DOCTYPE html>
<html>
<head>
<script>
	$(function() {
		setActiveNav('nav-job');
	});
</script>
</head>
<body>
  <div class="row">
    <div class="col-md-12">
      <h3 id="message" class="pull-left">${message}</h3>
      <form:form method="post" class="form-horizontal" modelAttribute="job">
        <div class="row">
          <div class="col-md-4">
            <t:select path="model" label="Model" options="${modelNameMappings}" />
            <t:input path="scenario" label="Scenario name" />
            <t:input path="study" label="Study name" />
            <t:input path="scenarioLocation" label="Scenario location" />
          </div>
          <div class="col-md-4">
            <t:input path="scenarioStartYear" label="Scenario start year" />
            <t:input path="scenarioEndYear" label="Scenario end year" />
            <t:input path="schedulingInformation" label="Scheduling information" />
          </div>
          <div class="col-md-4"></div>
        </div>
        <br />
        <button type="submit" class="btn btn-default">Submit</button>
      </form:form>
    </div>
  </div>

</body>
</html>
