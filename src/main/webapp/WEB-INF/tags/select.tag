<jsp:root version="2.0" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:form="http://www.springframework.org/tags/form"
  xmlns:spring="http://www.springframework.org/tags" xmlns:fn="http://java.sun.com/jsp/jstl/functions"
  xmlns:c="http://java.sun.com/jsp/jstl/core">

  <jsp:directive.tag description="Extended input tag to allow for sophisticated errors" pageEncoding="UTF-8" />
  <jsp:directive.attribute name="path" required="true" type="java.lang.String" />
  <jsp:directive.attribute name="cssClass" required="false" type="java.lang.String" />
  <jsp:directive.attribute name="label" required="false" type="java.lang.String" />
  <jsp:directive.attribute name="required" required="false" type="java.lang.Boolean" />
  <jsp:directive.attribute name="options" required="true" type="java.util.Map" />
  <c:if test="${empty label}">
    <c:set var="label"
      value="${fn:toUpperCase(fn:substring(path, 0, 1))}${fn:toLowerCase(fn:substring(path, 1,fn:length(path)))}" />
  </c:if>
  <spring:bind path="${path}">
    <div class="control-group ${status.error ? 'has-error' : '' }">
      <label class="control-label" for="${path}">${label}
        <c:if test="${required}">
          <span class="required">*</span>
        </c:if></label>
      <div class="controls">
        <form:select path="${path}" cssClass="${empty cssClass ? 'form-control' : cssClass}" items="${options}" />
        <c:if test="${status.error}">
          <span class="help-inline text-danger">${status.errorMessage}</span>
        </c:if>
      </div>
    </div>
  </spring:bind>
</jsp:root>
