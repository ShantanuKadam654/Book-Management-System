package com.example.BookManagement.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageController 
{

    private final ResourceLoader resourceLoader;

    public ImageController(ResourceLoader resourceLoader) 
    {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/images/{filename:.+}")
    public Resource getImage(@PathVariable String filename) 
    {
        Path path = Paths.get("D:/Sumago/Img/" + filename); 
        return resourceLoader.getResource("file:" + path.toString());
    }
}
