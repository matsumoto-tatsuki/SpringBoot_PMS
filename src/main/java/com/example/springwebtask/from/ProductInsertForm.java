package com.example.springwebtask.from;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductInsertForm {
    @NotBlank
    private String productId;
    @NotBlank
    private String productName;
    @NotBlank
    private String productPrice;
    @NotBlank
    private String categoryName;

    private String description;
}
