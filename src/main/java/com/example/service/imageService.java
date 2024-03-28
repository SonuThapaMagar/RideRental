//package com.example.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.model.Image;
//import com.example.repository.imageRepository;
//
//@Service
//public class imageService implements imageInterface{
//
//	  @Autowired
//	    private imageRepository imgRepo;
//
//	    @Override
//	    public Image create(Image image) {
//	        return imgRepo.save(image);
//	    }
//	    @Override
//	    public List<Image> viewAll() {
//	        return (List<Image>) imgRepo.findAll();
//	    }
//	    @Override
//	    public Image viewById(long id) {
//	        return imgRepo.findById(id).get();
//	    }
//	}
