package dev.iagorodrigues.dscatalog.services;

import dev.iagorodrigues.dscatalog.dto.CategoryDTO;
import dev.iagorodrigues.dscatalog.dto.ProductDTO;
import dev.iagorodrigues.dscatalog.entities.Category;
import dev.iagorodrigues.dscatalog.entities.Product;
import dev.iagorodrigues.dscatalog.exceptions.ResourceNotFoundException;
import dev.iagorodrigues.dscatalog.repositories.CategoryRepository;
import dev.iagorodrigues.dscatalog.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional
                .map(product -> new ProductDTO(product, product.getCategories()))
                .orElseThrow(() -> new ResourceNotFoundException("Não encontramos esse produto."));
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product product = new Product();
        mapProductDtoToEntity(productDTO, product);
        product = productRepository.save(product);
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product product = productRepository.getOne(id);
            mapProductDtoToEntity(productDTO, product);
            product = productRepository.save(product);
            return new ProductDTO(product, product.getCategories());
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("O produto com id " + id + " não existe");
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("O produto com id " + id + " não existe");
        }
    }

    private void mapProductDtoToEntity(ProductDTO productDTO, Product entity) {
        entity.setName(productDTO.getName());
        entity.setDate(productDTO.getDate());
        entity.setDescription(productDTO.getDescription());
        entity.setPrice(productDTO.getPrice());
        entity.setImgUrl(productDTO.getImgUrl());

        entity.getCategories().clear();
        for (CategoryDTO categoryDTO : productDTO.getCategories()) {
            Category category = categoryRepository.getOne(categoryDTO.getId());
            entity.getCategories().add(category);
        }
    }
}
