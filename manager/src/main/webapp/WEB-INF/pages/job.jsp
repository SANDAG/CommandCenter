<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
  <div class="row">
    <div class="col-md-12">
      <h3 id="message">${message}</h3>
      <form:form method="post" class="form-horizontal" modelAttribute="job">
        <div class="row">
          <div class="col-md-4">
            <t:select path="model" label="Model" options="${modelNameMappings}" />
            <t:input path="scenarioStartYear" label="Scenario start year" />
            <t:input path="scenarioEndYear" label="Scenario end year" />
            <t:input path="description" label="Description" />
          </div>
          <div class="col-md-8">
            <br />
            <button id="scenarioLocationPicker" class="btn btn-info" data-toggle="modal" data-target="#modalDirPicker">Scenario location chooser</button>
            <br />
            <t:input path="scenarioLocation" label="Scenario location" readonly="true" />
            <t:input path="study" label="Study name" readonly="true" />
            <t:input path="scenario" label="Scenario name" readonly="true" />
            <%@ include file="directoryPicker.jspf"%>
            <script>
            	dirPicker.onChosenCallback = function(path) {
            		var pathParts = path.split('/');
            		var endOffset = pathParts[pathParts.length - 1] == '' ? -2 : -1;
            		$('#scenarioLocation').val(path);
            		$('#study').val(pathParts[pathParts.length + endOffset - 1]);
            		$('#scenario').val(pathParts[pathParts.length + endOffset]);
            	};
            </script>
          </div>
        </div>
        <br />
        <button type="submit" class="btn btn-primary">Submit</button>
      </form:form>
    </div>
  </div>
</body>
</html>
