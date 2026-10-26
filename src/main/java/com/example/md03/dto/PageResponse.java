package com.example.md03.dto;

import java.util.List;

public class PageResponse<T> {
    private List<T> items;
    private Integer page;
    private Integer size;
    private Long totalItems;
    private Integer totalPages;
    private Boolean last;

    public PageResponse(List<T> items, Integer page, Integer size,
                        Long totalItems, Integer totalPages, Boolean last) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.last = last;
    }

    public List<T> getItems() { return items; }
    public Integer getPage() { return page; }
    public Integer getSize() { return size; }
    public Long getTotalItems() { return totalItems; }
    public Integer getTotalPages() { return totalPages; }
    public Boolean getLast() { return last; }
}