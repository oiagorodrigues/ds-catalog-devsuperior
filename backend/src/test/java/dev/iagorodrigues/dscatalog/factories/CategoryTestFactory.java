package dev.iagorodrigues.dscatalog.factories;

import dev.iagorodrigues.dscatalog.entities.Category;

public class CategoryTestFactory {

    public static Category createCategory() {
        return new Category(1L, "Electronics");
    }
}
