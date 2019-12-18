package com.zl.jpa.common.dao;

import java.io.Serializable;
import java.util.Map;

/**
 * 参数公共类
 */
public class ParamsMap implements Serializable {

    private static final long serialVersionUID = 4158031937649732884L;
    
    private Integer pageSize;
    
    private Integer pageNumber;
    
    private Integer totalPage;
    
    private Map<String , Object> params;

    public Integer getPageSize() {
        if(null == this.pageSize){
            this.pageSize = 10;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        if(null == this.pageNumber){
            this.pageNumber = 1;
        }
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    
    

}
