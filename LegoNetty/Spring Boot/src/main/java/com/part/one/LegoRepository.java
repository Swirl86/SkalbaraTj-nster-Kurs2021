package com.part.one;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LegoRepository {

    public HashMap<Integer, Lego> legoList;
    public HashMap<Lego, List<Lego>> categoryList;

    public LegoRepository() throws FileNotFoundException {
        this.legoList = new HashMap<>();
        categoryList = new HashMap<>();
        getDataFromFile();
    }

    public Collection<Lego> getAllLego() {
        return legoList.values();
    }

    public HashMap<Lego, List<Lego>> getAllLegoByCategory() {
        return categoryList;
    }

    public void getDataFromFile() throws FileNotFoundException {
        File file = new File("themes.csv");

        Scanner fileRead = new Scanner(file);
        String textString = fileRead.nextLine(); // Skip first line -> id,name,parent_id
        while (fileRead.hasNextLine()) {
            textString = fileRead.nextLine();
            String[] row = textString.split(",");

            Lego lego = new Lego();
            lego.setId(Integer.parseInt(row[0]));
            lego.setName(row[1]);
            if(row.length == 3) {
                lego.setParentId(Integer.parseInt(row[2]));
            } else {
                lego.setParentId(0); // If no parentId they are the parent, no id starts at 0 easy to search for
            }
            legoList.put(lego.getId(), lego);
        }
        fileRead.close();
        createCategoryRelations();
    }

    private void createCategoryRelations() {
        List<Lego> keys = new ArrayList<>();
        keys = legoList.values().stream()
                .filter(x-> x.getParentId() == 0)
                .collect(Collectors.toList());

        for (Lego keyItem: keys) {
            HashMap<Integer, Lego> legoListCopy = new HashMap<>(legoList);
            List<Lego> list = legoListCopy.values().stream()
                        .filter(value -> value.getParentId() == keyItem.getId())
                        .collect(Collectors.toList());
            categoryList.put(keyItem, list);
        }
    }
}
