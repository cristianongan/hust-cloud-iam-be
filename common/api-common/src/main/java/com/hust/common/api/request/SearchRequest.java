package com.hust.common.api.request;

import com.hust.common.util.QueryUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
public class SearchRequest extends Request {

	@Serial
    private static final long serialVersionUID = -1039582106977754111L;
	
	private List<Long> idBag;

    private Long id;

    private String keyword;

    private String orderByType;

    private String orderByColumn;
    
    private String provinceCode;
    
    private String districtCode;
    
    private String wardCode;
    
    private Integer status;

    private Long deptId;

    private int pageIndex = QueryUtil.FIRST_INDEX;

    private int pageSize = QueryUtil.PAGE_SIZE_DEFAULT;

}
