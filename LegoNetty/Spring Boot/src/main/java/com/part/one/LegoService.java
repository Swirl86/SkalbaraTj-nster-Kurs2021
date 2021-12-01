package com.part.one;

import com.part.one.dto.CategoryDto;
import com.part.one.dto.LegoCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LegoService {

    @Autowired
    LegoRepository repository;

    public Collection<Lego> getAllLego() {
        return repository.getAllLego().stream()
                .sorted(Comparator.comparingInt(Lego::getId))
                .collect(Collectors.toList());
    }

    public Collection<CategoryDto> getAllLegoCategories() {
        return repository.getAllLegoByCategory().entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<Lego, List<Lego>> e) -> e.getKey().getId())
                        .thenComparing(e -> e.getKey().getId()))
                .map(Map.Entry::getKey)
                .map(x -> new CategoryDto(x.getId(), x.getName()))
                .collect(Collectors.toList());
    }

    public Collection<LegoCategoryDto> getAllLegoInCategories() {
        HashMap<Lego, List<Lego>> categoryList = repository.getAllLegoByCategory();

        categoryList = categoryList.entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<Lego, List<Lego>> e) -> e.getKey().getId())
                        .thenComparing(e -> e.getKey().getId()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));

        return getLegoDtoList(categoryList);
    }

    private String getValueString(List<Lego> value) {
        if (value.size() == 0) return "[]";
        StringBuilder str = new StringBuilder();
        str.append("|");
        for (Lego lego : value) {
            String id = "  ID - " + lego.getId();
            String name = " : " + lego.getName();
            str.append(id).append(name).append("  |");
        }
        return str.toString();
    }

    public Collection<Lego> getLegoRelatedById(int id) {
        return repository.getAllLego()
                .stream()
                .filter(entry -> entry.getId() == id || entry.getParentId() == id)
                .sorted(Comparator.comparingInt(Lego::getId))
                .collect(Collectors.toList());
    }

    public Collection<Lego> getLegoMatches(String searchStr) {
        return repository.getAllLego()
                .stream()
                // Wildcard search - match anything before and after the searchStr
                .filter(entry -> entry.getName().toLowerCase().matches(".*" + searchStr.toLowerCase() + ".*"))
                .sorted(Comparator.comparingInt(Lego::getId))
                .collect(Collectors.toList());
    }

    public Collection<LegoCategoryDto> getLegoCategoryMatches(String searchStr) {
        HashMap<Lego, List<Lego>> categoryList = repository.getAllLegoByCategory();

        // Sort by id
        categoryList = repository.getAllLegoByCategory().entrySet().stream()
                .filter(entry -> entry.getKey().getName().toLowerCase().matches(".*" + searchStr.toLowerCase() + ".*") )
                .sorted(Comparator.comparing((Map.Entry<Lego, List<Lego>> e) -> e.getKey().getId())
                        .thenComparing(e -> e.getKey().getId()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));

        return getLegoDtoList(categoryList);
    }

    private List<LegoCategoryDto> getLegoDtoList(HashMap<Lego, List<Lego>> categoryList) {
        List<LegoCategoryDto> dtoList = new ArrayList<>(); // Create dto response
        for (HashMap.Entry<Lego, List<Lego>> entry : categoryList.entrySet()) {
            // Build wanted response structure
            String category = "ID - " + entry.getKey().getId() + " : " + entry.getKey().getName();
            String values = getValueString(entry.getValue());
            dtoList.add(new LegoCategoryDto(category, values));
        }
        return dtoList;
    }
}
