package dev.iagorodrigues.dscatalog.controllers;

import dev.iagorodrigues.dscatalog.dto.ProductDTO;
import dev.iagorodrigues.dscatalog.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> fetchProducts(
            @RequestParam(value = "category", defaultValue = "0") Long categoryId,
            @RequestParam(defaultValue = "") String name,
            Pageable pageable
    ) {
        Page<ProductDTO> products = service.fetchProducts(categoryId, name.trim(), pageable);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable Long id) {
        ProductDTO productDTO = service.findById(id);
        return ResponseEntity.ok().body(productDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        ProductDTO createdProductDTO = service.insert(productDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdProductDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(createdProductDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO) {
        ProductDTO updatedProductDTO = service.update(id, productDTO);
        return ResponseEntity.ok().body(updatedProductDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
