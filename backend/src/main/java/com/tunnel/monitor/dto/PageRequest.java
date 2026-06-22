package com.tunnel.monitor.dto;

import lombok.Data;

@Data
public class PageRequest {

    private Integer page = 1;

    private Integer size = 10;

    private String sortBy;

    private String sortOrder = "desc";

    public int getPageIndex() {
        return Math.max(page - 1, 0);
    }
}
