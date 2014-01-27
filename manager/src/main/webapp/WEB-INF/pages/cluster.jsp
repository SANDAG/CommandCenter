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
      <h3 id="message" class="pull-left">${message}</h3>
      <form:form method="post" class="form-horizontal" modelAttribute="cluster">
        <div class="row">
          <div class="col-md-4">
            <t:input path="name" label="Host" />
          </div>
          <div class="col-md-4"></div>
          <div class="col-md-4"></div>
        </div>
        <br />
        <button type="submit" class="btn btn-default">Submit</button>
      </form:form>
    </div>
  </div>

</body>
</html>
