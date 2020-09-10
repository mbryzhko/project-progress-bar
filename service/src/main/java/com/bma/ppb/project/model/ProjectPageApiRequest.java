package com.bma.ppb.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
public class ProjectPageApiRequest {

    private final int page;
    private final int size;

    public Pageable getPageable() {
        return PageRequest.of(page, size);
    }

}
