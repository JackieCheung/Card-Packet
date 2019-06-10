package com.bcms.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * @className PageableBuilder
 * @description: 分页构造器
 * @author: Jackie
 * @date: 2018/12/22 15:02
 */
@Data
@NoArgsConstructor
public class PageableBuilder {
    /**
     * 默认排序字段
     */
    private static final String DEFAULT_SORT_FIELD = "id";
    /**
     * 默认排序方向
     */
    private static final String DEFAULT_SORT_DIR = "DESC";
    /**
     * 页码，默认为1
     */
    private Integer pageNum = 1;
    /**
     * 每页大小，默认为10
     */
    private Integer pageSize = 10;
    /**
     * 排序字段，默认根据id排序
     */
    private String sort = DEFAULT_SORT_FIELD;
    /**
     * 排序方向，默认降序
     */
    private String dir = DEFAULT_SORT_DIR;

    /**
     * @Description: 根据类中的字段，返回处理后的 jpa 分页查询所需要的 Pageable 分页参数
     * @Params: []
     * @Return: Pageable
     * @Author: Jackie
     * @Date: 2018/12/22 15:05
     */
    public Pageable getPageable() {
        /*
         * 排序项
         */
        Sort sort = Sort.by(Direction.ASC, this.sort);

        if (DEFAULT_SORT_DIR.equals(this.dir)) {
            sort = Sort.by(Direction.DESC, this.sort);
        }
        return PageRequest.of(this.pageNum - 1, this.pageSize, sort);
    }

    public PageableBuilder buildPage(Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public PageableBuilder buildPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageableBuilder buildSort(String sort) {
        this.sort = sort;
        return this;
    }

    public PageableBuilder buildDir(String dir) {
        this.dir = dir;
        return this;
    }

    public PageableBuilder buildSort(String sort, String dir) {
        this.sort = sort;
        this.dir = dir;
        return this;
    }
}
