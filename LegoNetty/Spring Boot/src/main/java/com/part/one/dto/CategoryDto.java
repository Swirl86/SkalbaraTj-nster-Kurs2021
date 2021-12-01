package com.part.one.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    int id;
    String name;

    public CategoryDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
