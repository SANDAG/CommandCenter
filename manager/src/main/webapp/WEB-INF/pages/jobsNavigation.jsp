<ul class="nav nav-pills">
  <li id="jobsNavQueued"><a href="<c:url value="/jobs/queued"/>">Queued</a></li>
  <li id="jobsNavRunning"><a href="<c:url value="/jobs/running"/>">Running</a></li>
</ul>
<script>
	$(function () {
		var pathToLiId = {'jobs/queued': '#jobsNavQueued', 'jobs/running': '#jobsNavRunning'};
		var urlPath = window.location.pathname;
		
		for (var path in pathToLiId) {
			if (urlPath.indexOf(path) > -1) {
				$(pathToLiId[path]).addClass('active');
				return;
			}
		}
	});
</script>