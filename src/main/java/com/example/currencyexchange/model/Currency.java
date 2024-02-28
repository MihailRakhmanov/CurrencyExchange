package com.example.currencyexchange.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Currency {
    private Integer id;
    @NonNull
    @Size(min = 3, max = 3, message = "Сode should be 3 symbols long")
    private String code;
    @NonNull
    @NotNull(message = "FullName should not be null")
    @NotBlank(message = "FullName should not be empty")
    private String fullName;
    @NonNull
    @Size(min = 1, max = 1, message = "Sign should be 1 symbol long")
    private String sign;

}
