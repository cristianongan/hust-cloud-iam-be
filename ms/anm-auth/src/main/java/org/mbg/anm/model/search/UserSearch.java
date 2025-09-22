package org.mbg.anm.model.search;

import lombok.Data;

import java.util.List;

@Data
public class UserSearch {
    private String username;

    private String name;

    private String email;

    private String phone;

    private List<Integer> statuses;

    private String keyword;

    private List<Long> ids;

    private int page;

    private int pageSize;
}
