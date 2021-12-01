package com.part.one.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LegoCategoryDto implements Serializable {
    private String category;
    private String legos;

    public LegoCategoryDto(String category, String legos) {
        this.category = category;
        this.legos = legos;
    }
}
