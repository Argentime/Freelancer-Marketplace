package com.example.javalabs.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api")
public class Controller {
    @GetMapping("/freelancers")
    public String query(@RequestParam String category) {
      switch (category) {
          case "design":

      }
    }
}
