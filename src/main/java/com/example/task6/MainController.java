package com.example.task6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class MainController {

    @Autowired
    private MainService service;

    @GetMapping("/locales")
    @ResponseStatus(HttpStatus.OK)
    public List<ELocale> getLocales() {
        return new ArrayList<>(Arrays.asList(ELocale.values()));
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> generateUsers(
            @RequestParam(name = "locale", defaultValue = "ru") ELocale locale,
            @RequestParam(name = "count", defaultValue = "20") int count,
            @RequestParam(name = "seed", defaultValue = "0") long seed,
            @RequestParam(name = "error", defaultValue = "0") double error
    ) {
        return service.generateUsers(locale, count, seed, error);
    }

}
