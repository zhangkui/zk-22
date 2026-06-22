package com.tunnel.monitor.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {

    private List<T> records;

    private long total;

    private int page;

    private int size;

    private int totalPages;

    public PageResult() {
    }

    public PageResult(List<T> records, long total, int page, int size) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.size = size;
        this.totalPages = (int) Math.ceil((double) total / size);
    }

    public static <T> PageResult<T> of(List<T> records, long total, int page, int size) {
        return new PageResult<>(records, total, page, size);
    }
}
