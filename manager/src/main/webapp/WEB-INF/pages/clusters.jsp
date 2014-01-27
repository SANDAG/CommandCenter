<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html>
<html>
<head>
<script>
	$(function() {
		$('.toggleActiveButton').click(function() {
			toggleActive($(this));
		});
		$('.deleteButton').click(function() {
			deleteCluster($(this));
		});
	});

	function toggleActive(button) {
		var row = button.closest('tr');
		var id = row.data('id');
		var errorFunction = function(e) {
			alert('There was an error deactivating that cluster; please try again later.');
		};
		var successFunction = function() {
			var isOn = button.hasClass('off');
			button.removeClass(isOn ? 'off' : 'on');
			button.addClass(isOn ? 'on' : 'off');
			button.attr('title', isOn ? 'Deactivate' : 'Activate');
		};

		$.ajax({
			type : 'GET',
			url : '<c:url value="/admin/cluster/toggle/"/>' + id,
			error : errorFunction,
			success : successFunction
		});
	}

	function deleteCluster(button) {
		var row = button.closest('tr');
		var id = row.data('id');
		var successFunction = function(response) {
			row.remove();
			$('#message').text(response);
		};
		var errorFunction = function(e) {
			alert('There was an error deleting the chosen cluster; please try again later.');
		};

		$.ajax({
			type : 'DELETE',
			url : '<c:url value="/admin/cluster/"/>' + id,
			success : successFunction,
			error : errorFunction
		});
	}
</script>
<style>
.btn.off i {
	text-color: gray;
}
</style>
</head>
<body>
  <div class="row">
    <div class="col-md-8"></div>
    <div class="col-md-4">
      <a class="btn btn-success pull-right" href="<c:url value="/admin/cluster" />"><i
        class="glyphicon glyphicon-plus"></i> Add Cluster</a>
    </div>
  </div>
  <div class="row">
    <div class="col-md-12">
      <h3 id="message" class="pull-left"></h3>
      <table class="table table-striped">
        <thead>
          <tr>
            <th>Host</th>
            <th colspan="99"></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${clusters}" var="cluster" varStatus="status">
            <tr data-id="${cluster.id}">
              <td>${cluster.name}</td>
              <td class="button"><a class="btn btn-primary btn-xs toggleActiveButton ${cluster.active ? 'on' : 'off'}"
                title="${cluster.active ? 'Deactivate' : 'Activate'}"> <span class="glyphicon glyphicon-off"></span>
              </a></td>
              <td class="button">
                <button type="button" class="btn btn-danger btn-xs deleteButton" title="Delete">
                  <span class="glyphicon glyphicon-trash"></span>
                </button>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</body>
</html>
