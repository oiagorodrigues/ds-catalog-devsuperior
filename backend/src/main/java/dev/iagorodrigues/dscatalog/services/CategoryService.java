package dev.iagorodrigues.dscatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.iagorodrigues.dscatalog.dto.CategoryDTO;
import dev.iagorodrigues.dscatalog.entities.Category;
import dev.iagorodrigues.dscatalog.exceptions.EntityNotFoundException;
import dev.iagorodrigues.dscatalog.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.map(CategoryDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Não encontramos essa categoria"));
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category entity = new Category(categoryDTO);
        entity = categoryRepository.save(entity);
        return new CategoryDTO(entity);
    }
}
