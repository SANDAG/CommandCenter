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
      <h3>Job details</h3>
      <dl class="dl-horizontal" class="well">
        <dt>Study years</dt>
        <dd>${job.scenarioStartYear} to ${job.scenarioEndYear}</dd>
        <dt>Scenario location</dt>
        <dd class='longUrl'>${job.scenarioLocation}</dd>
        <dt>Description</dt>
        <dd>${job.description}</dd>
      </dl>
    </div>
  </div>
  <div class="row">
    <div class="col-md-12">
      <h3 id="message">${message}</h3>
      <form:form method="post" class="form-horizontal" modelAttribute="job">
        <div class="row">
          <div class="col-md-12">
            <t:input path="archivedNotes" label="Archive notes" />
          </div>
        </div>
        <br />
        <button type="submit" class="btn btn-primary">Submit</button>
      </form:form>
    </div>
  </div>
</body>
</html>
