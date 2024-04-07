	document.addEventListener("DOMContentLoaded", function () {
	    const passwordInput = document.getElementById("passwordInput");
	    const togglePassword = document.getElementById("togglePassword");
	
	    togglePassword.addEventListener("click", function () {
	      const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
	      passwordInput.setAttribute("type", type);
	      // Toggle the eye icon based on the password visibility
	      this.classList.toggle("fa-eye-slash");
	    });
	
	    const sign_in_btn = document.querySelector("#sign-in-btn");
	    const sign_up_btn = document.querySelector("#sign-up-btn");
	    const container = document.querySelector(".container");
	
	    sign_up_btn.addEventListener("click", () => {
	      container.classList.add("sign-up-mode");
	    });
	
	    sign_in_btn.addEventListener("click", () => {
	      container.classList.remove("sign-up-mode");
	    });
	    
	    
	    
	});
