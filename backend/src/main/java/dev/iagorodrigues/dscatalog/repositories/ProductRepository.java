package dev.iagorodrigues.dscatalog.repositories;

import dev.iagorodrigues.dscatalog.entities.Category;
import dev.iagorodrigues.dscatalog.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p FROM Product p INNER JOIN p.categories cats " +
            "WHERE (coalesce(:categories) IS NULL OR cats IN :categories) " +
            "AND (:name = '' OR lower(p.name) LIKE lower(concat('%', :name, '%'))) ")
    Page<Product> fetchProducts(List<Category> categories, String name, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.categories WHERE p IN :products")
    List<Product> findCategoriesForProducts(List<Product> products);
}
