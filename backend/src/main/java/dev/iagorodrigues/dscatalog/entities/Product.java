package dev.iagorodrigues.dscatalog.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;

    private List<Category> categories;

}
