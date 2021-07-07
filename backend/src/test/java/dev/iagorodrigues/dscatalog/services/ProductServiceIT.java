package dev.iagorodrigues.dscatalog.services;

import dev.iagorodrigues.dscatalog.dto.ProductDTO;
import dev.iagorodrigues.dscatalog.exceptions.ResourceNotFoundException;
import dev.iagorodrigues.dscatalog.factories.ProductTestFactory;
import dev.iagorodrigues.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private ProductDTO productDTO;
    private int pageNumber;
    private int pageSize;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
        pageNumber = 0;
        pageSize = 10;
        productDTO = ProductTestFactory.createProductDTO();
    }

    @Test
    public void fetchProductsShouldReturnPageWithProductsWhenPage0Size10TotalElements25() {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<ProductDTO> result = service.fetchProducts(0L, "", pageRequest);

        assertFalse(result.isEmpty());
        assertEquals(pageNumber, result.getNumber());
        assertEquals(pageSize, result.getSize());
        assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void fetchProductsShouldReturnPageFilteredByProducts() {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<ProductDTO> result = service.fetchProducts(2L, "", pageRequest);

        assertFalse(result.isEmpty());
        assertEquals(pageNumber, result.getNumber());
        assertEquals(pageSize, result.getSize());
        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void fetchProductsShouldReturnPageFilteredByProductName() {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<ProductDTO> result = service.fetchProducts(0L, "Gamer", pageRequest);

        assertFalse(result.isEmpty());
        assertEquals(pageNumber, result.getNumber());
        assertEquals(pageSize, result.getSize());
        assertEquals(21, result.getTotalElements());
    }

    @Test
    public void fetchProductsShouldReturnEmptyPageWhenPageDoesNotExists() {
        PageRequest pageRequest = PageRequest.of(50, pageSize);
        Page<ProductDTO> result = service.fetchProducts(0L, "", pageRequest);
        assertTrue(result.isEmpty());
    }

    @Test
    public void fetchProductsShouldReturnSortedPageWhenSortByName() {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("name"));

        Page<ProductDTO> result = service.fetchProducts(0L, "", pageRequest);

        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.findById(existingId);
        assertNotNull(result);
        assertEquals("The Lord of the Rings", result.getName());
        assertEquals(90.5, result.getPrice());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class,
                () -> service.findById(nonExistingId));
    }

    @Test
    public void insertShouldReturnProductDTO() {
        ProductDTO result = service.insert(productDTO);

        assertNotNull(result);
        assertEquals(productDTO.getName(), result.getName());
        assertEquals(productDTO.getPrice(), result.getPrice());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.update(existingId, productDTO);

        assertEquals(productDTO.getId(), result.getId());
        assertEquals(productDTO.getName(), result.getName());
        assertEquals(productDTO.getPrice(), result.getPrice());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class,
                () -> service.update(nonExistingId, productDTO));
    }

    @Test
    public void deleteShouldDeleteProductWhenIdExists() {
        service.delete(existingId);
        assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
    }

}
