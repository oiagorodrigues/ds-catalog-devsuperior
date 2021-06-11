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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private final long existingId = 1L;
    private final long nonExistingId = 2L;
    private final long dependentId = 3L;
    private final ProductDTO productDTO = ProductTestFactory.createProductDTO();
    private final Product product = ProductTestFactory.createProduct();
    private final Category category = CategoryTestFactory.createCategory();
    private final PageImpl<Product> page = new PageImpl<>(List.of(product));

    @BeforeEach
    void setUp() {
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.getOne(existingId)).thenReturn(product);
        Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void findAllShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAll(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Mockito.verify(repository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.findById(nonExistingId));
        Mockito.verify(repository).findById(nonExistingId);
    }

    @Test
    public void insertShouldReturnProductDTO() {
        ProductDTO productDTO = this.productDTO;
        productDTO.setId(null);

        ProductDTO result = service.insert(productDTO);

        Assertions.assertNotNull(result);

        Mockito.verify(categoryRepository).getOne(existingId);
        Mockito.verify(repository).save(ArgumentMatchers.any());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.update(existingId, productDTO);

        Assertions.assertNotNull(result);

        Mockito.verify(categoryRepository).getOne(existingId);
        Mockito.verify(repository).getOne(existingId);
        Mockito.verify(repository).save(ArgumentMatchers.any());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.update(nonExistingId, productDTO));
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> service.delete(existingId));
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> service.delete(nonExistingId));

        Mockito.verify(repository).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(DatabaseException.class,
                () -> service.delete(dependentId));

        Mockito.verify(repository).deleteById(dependentId);
    }
}
