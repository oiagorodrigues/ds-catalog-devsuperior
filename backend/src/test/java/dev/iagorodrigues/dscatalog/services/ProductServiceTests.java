package dev.iagorodrigues.dscatalog.services;

import dev.iagorodrigues.dscatalog.dto.ProductDTO;
import dev.iagorodrigues.dscatalog.entities.Category;
import dev.iagorodrigues.dscatalog.entities.Product;
import dev.iagorodrigues.dscatalog.exceptions.DatabaseException;
import dev.iagorodrigues.dscatalog.exceptions.ResourceNotFoundException;
import dev.iagorodrigues.dscatalog.factories.CategoryTestFactory;
import dev.iagorodrigues.dscatalog.factories.ProductTestFactory;
import dev.iagorodrigues.dscatalog.repositories.CategoryRepository;
import dev.iagorodrigues.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private ProductDTO productDTO;
    private Product product;
    private Category category;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        productDTO = ProductTestFactory.createProductDTO();
        product = ProductTestFactory.createProduct();
        category = CategoryTestFactory.createCategory();
        page = new PageImpl<>(List.of(product));

        when(repository.fetchProducts(any(), any(), (Pageable) any())).thenReturn(page);

        when(repository.findById(existingId)).thenReturn(Optional.of(product));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(repository.getOne(existingId)).thenReturn(product);
        when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(repository.save(any())).thenReturn(product);

        doNothing().when(repository).deleteById(existingId);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        when(categoryRepository.getOne(existingId)).thenReturn(category);
        when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void fetchProductsShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.fetchProducts(0L, "", pageable);

        assertNotNull(result);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.findById(existingId);

        assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class,
                () -> service.findById(nonExistingId));
    }

    @Test
    public void insertShouldReturnProductDTO() {
        ProductDTO productDTO = this.productDTO;
        productDTO.setId(null);

        ProductDTO result = service.insert(productDTO);

        assertNotNull(result);
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.update(existingId, productDTO);

        assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class,
                () -> service.update(nonExistingId, productDTO));
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> service.delete(existingId));
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class,
                () -> service.delete(nonExistingId));
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExists() {
        assertThrows(DatabaseException.class,
                () -> service.delete(dependentId));
    }
}
