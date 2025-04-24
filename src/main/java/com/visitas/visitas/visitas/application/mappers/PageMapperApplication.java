package com.visitas.visitas.visitas.application.mappers;

import com.visitas.visitas.visitas.domain.utils.page.PagedResult;

import java.util.List;

public class PageMapperApplication {
    public <T> PagedResult<T> fromPage(List<T> content, PagedResult<?> r) {
        return new PagedResult<>(
                content,
                r.getPage(),
                r.getSize(),
                r.getTotalElements()
        );
    }
}
