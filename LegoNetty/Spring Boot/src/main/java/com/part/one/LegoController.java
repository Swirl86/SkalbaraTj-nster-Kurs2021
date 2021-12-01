package com.part.one;

import com.part.one.dto.CategoryDto;
import com.part.one.dto.LegoCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.net.InetSocketAddress;
import java.util.Collection;

@RestController
@RequestMapping("/lego")
public class LegoController {

    public final int VALID_REQUEST_PORT = 6000; // Make sure same port number as proxy

    @Autowired
    LegoService service;

    @GetMapping("/all-lego")
    public Collection<Lego> getAllLego(@RequestHeader HttpHeaders headers) {
        checkIfValidHeaders(headers);
        System.out.println("Show all lego and categories");
        return service.getAllLego();
    }

    @GetMapping("/all-categories")
    public Collection<CategoryDto> getAllLegoCategories(@RequestHeader HttpHeaders headers) {
        checkIfValidHeaders(headers);
        System.out.println("Show all lego categories");
        return service.getAllLegoCategories();
    }

    @GetMapping("/all-lego-by-categories")
    public Collection<LegoCategoryDto> getAllLegoInCategories(@RequestHeader HttpHeaders headers) {
        checkIfValidHeaders(headers);
        System.out.println("Show all lego in related category");
        return service.getAllLegoInCategories();
    }

    @GetMapping("/search-by-id/{id}")
    public Collection<Lego> getLegoRelatedById(@PathVariable String id,
                                               @RequestHeader HttpHeaders headers,
                                               HttpServletResponse response) {
        checkIfValidHeaders(headers);
        if (!isNumeric(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request, Not a number!");

        System.out.println("Show all lego and category related to id");
        return service.getLegoRelatedById(Integer.parseInt(id));
    }

    @GetMapping("/search-by-name/{searchStr}")
    public Collection<Lego> getLegoByNameMatches(@PathVariable String searchStr,
                                                 @RequestHeader HttpHeaders headers,
                                                 HttpServletResponse response) {
        checkIfValidHeaders(headers);
        System.out.println("Show all lego containing input string");
        return service.getLegoMatches(searchStr);
    }

    @GetMapping("/search-by-category/{searchStr}")
    public Collection<LegoCategoryDto> getLegoByCategoryMatches(@PathVariable String searchStr,
                                                                @RequestHeader HttpHeaders headers,
                                                                HttpServletResponse response) {
        checkIfValidHeaders(headers);
        System.out.println("Show all lego connected to category");
        return service.getLegoCategoryMatches(searchStr);
    }

    private void checkIfValidHeaders(HttpHeaders headers) {
        InetSocketAddress host = headers.getHost();
        if (host == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        // If request is not from proxy port it is not valid
        if (host.getPort() != VALID_REQUEST_PORT)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not proxy!");
    }

    public boolean isNumeric(String string) {
        int intValue;
        if (string == null || string.equals("")) return false;

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
