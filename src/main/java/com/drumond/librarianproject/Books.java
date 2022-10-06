package com.drumond.librarianproject;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Books {
    private final int id;
    private final String title;
    private final String author;
    private final int year;
    private final int pages;
}