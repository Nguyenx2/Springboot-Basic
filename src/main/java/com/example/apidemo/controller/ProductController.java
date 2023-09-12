package com.example.apidemo.controller;

import com.example.apidemo.models.Product;
import com.example.apidemo.models.ResponseObject;
import com.example.apidemo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    List<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getProductWithId(@PathVariable Long id){
        return productService.findById(id);
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> addProduct(@RequestBody Product newProduct){
        return productService.insertProduct(newProduct);
    }

}
