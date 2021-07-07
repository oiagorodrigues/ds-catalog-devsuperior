package dev.iagorodrigues.dscatalog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.iagorodrigues.dscatalog.dto.ProductDTO;
import dev.iagorodrigues.dscatalog.factories.ProductTestFactory;
import dev.iagorodrigues.dscatalog.tests.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private String requestJsonBody;
    private ProductDTO productDTO;
    private String username;
    private String password;

    @BeforeEach
    void setUp() throws Exception {
        username = "maria@gmail.com";
        password = "123456";
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
        productDTO = ProductTestFactory.createProductDTO();
        requestJsonBody = objectMapper.writeValueAsString(productDTO);
    }

    @Test
    public void fetchProductsShouldReturnSortedPageWhenSortByName() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }

    @Test
    public void findProductByIdShouldReturnProductDTOWhenIdExists() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value("The Lord of the Rings"));
        result.andExpect(jsonPath("$.price").value(90.5));
    }

    @Test
    public void findProductByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void createProductByIdShouldReturnProductDTO() throws Exception {
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions result =
                mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(requestJsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").value(countTotalProducts + 1));
        result.andExpect(jsonPath("$.name").value(productDTO.getName()));
        result.andExpect(jsonPath("$.description").value(productDTO.getDescription()));
        result.andExpect(jsonPath("$.price").value(productDTO.getPrice()));
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions result =
                mockMvc.perform(put("/products/{id}", existingId)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(requestJsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(productDTO.getName()));
        result.andExpect(jsonPath("$.description").value(productDTO.getDescription()));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions result =
                mockMvc.perform(put("/products/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(requestJsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions result =
                mockMvc.perform(delete("/products/{id}", existingId)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions result =
                mockMvc.perform(delete("/products/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }
}
