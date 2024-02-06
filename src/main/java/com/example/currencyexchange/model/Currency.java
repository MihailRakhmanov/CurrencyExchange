package com.example.currencyexchange.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Currency {
    private Integer id;
    @NonNull
    private String code;
    @NonNull
    private String fullName;
    @NonNull
    private String sign;

}
