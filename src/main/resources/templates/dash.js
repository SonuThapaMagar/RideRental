// SIDEBAR TOGGLE

let sidebarOpen = false;
const sidebar = document.getElementById('sidebar');

function openSidebar() {
  if (!sidebarOpen) {
    sidebar.classList.add('sidebar-responsive');
    sidebarOpen = true;
  }
}

function closeSidebar() {
  if (sidebarOpen) {
    sidebar.classList.remove('sidebar-responsive');
    sidebarOpen = false;
  }
}
	 const toggle = () => classList.toggle("active");

	  window.addEventListener("click", function (e) {
	    if (!btn.contains(e.target)) classList.remove("active");
	  });
	  
		function clearSearch() {
			document.getElementById('search').value = ''; // Clear search input
			document.querySelector('form').submit();
		}