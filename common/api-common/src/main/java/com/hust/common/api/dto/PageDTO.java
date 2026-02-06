package com.hust.common.api.dto;

import com.hust.common.base.annotation.Exclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class PageDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 2117490071837152141L;

    @Exclude
    private List<T> content;

    Pageable pageable;

    private Integer totalElements;

    @Getter
    @Setter
    public static class Pageable {
        private Integer pageNumber;

        private Integer pageSize;
    }

    public Page<T> getPage() {
        PageRequest pageRequest = Optional.ofNullable(this.pageable)
                .map(p -> PageRequest.of(p.getPageNumber(), p.getPageSize()))
                .orElseGet(() -> PageRequest.of(0, this.content.size()));

        return new PageImpl<>(this.content, pageRequest, this.totalElements);
    }

}
