package com.example.apidemo.services;

import com.example.apidemo.models.Product;
import com.example.apidemo.models.ResponseObject;
import com.example.apidemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

//    Get Product With Id
    public ResponseEntity<ResponseObject> findById(Long id){
        Optional<Product> foundProduct = productRepository.findById(id);
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Query product successfully", foundProduct)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED", "Cannot find product with id=" + id, "")
                );
    }

//    Post Product
    public ResponseEntity<ResponseObject> insertProduct(Product newProduct){
        List<Product> foundProduct = productRepository.findByProductName(newProduct.getProductName());
        if (!foundProduct.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Insert product successfully !", productRepository.save(newProduct))
        );
    }

//    Update product
    public ResponseEntity<ResponseObject> updateProduct(Product newProduct, Long id) {
        Product updatedProduct = productRepository.findById(id)
                .map(product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setQuantity(newProduct.getQuantity());
                    product.setPrice(newProduct.getPrice());
                    return product;
                })
                .orElseGet(() -> {
                    newProduct.setId(id);
                    return newProduct;
                });

        updatedProduct = productRepository.save(updatedProduct);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update Product successfully", updatedProduct)
        );
    }

// Delete Product
    public ResponseEntity<ResponseObject> deleteProduct(Long id) {
        boolean exist = productRepository.existsById(id);
        String message;
        HttpStatus status;

        if (exist) {
            productRepository.deleteById(id);
            message = "Delete product successfully";
            status = HttpStatus.OK;
        } else {
            message = "Cannot find product to delete";
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(
                new ResponseObject(exist ? "OK" : "failed", message, "")
        );
    }
}
