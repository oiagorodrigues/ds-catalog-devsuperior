package dev.iagorodrigues.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.iagorodrigues.dscatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
