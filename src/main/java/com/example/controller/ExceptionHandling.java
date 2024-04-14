//package com.example.controller;
//
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class ExceptionHandling {
//	  @ExceptionHandler(Exception.class)
//	    public String handleException(Exception e, Model model) {
//	        // Log the exception for debugging purposes
//	        e.printStackTrace();
//	        
//	        // Add error message to the model
//	        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
//	        
//	        // Return the error view
//	        return "error";
//	    }
//}
