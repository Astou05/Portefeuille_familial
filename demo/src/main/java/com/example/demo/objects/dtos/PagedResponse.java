package com.example.demo.objects.dtos;

import org.springframework.data.domain.Page;
import java.util.List;

public class PagedResponse<T> {

    private List<T> content;       // les données de la page actuelle
    private int currentPage;       // numéro de la page (commence à 0)
    private int pageSize;          // nombre d'éléments par page
    private long totalElements;    // nombre total d'éléments en base
    private int totalPages;        // nombre total de pages

    public PagedResponse(Page<T> page) {
        this.content       = page.getContent();
        this.currentPage   = page.getNumber();
        this.pageSize      = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages    = page.getTotalPages();
    }

    public List<T> getContent()       { return content; }
    public int getCurrentPage()       { return currentPage; }
    public int getPageSize()          { return pageSize; }
    public long getTotalElements()    { return totalElements; }
    public int getTotalPages()        { return totalPages; }
}