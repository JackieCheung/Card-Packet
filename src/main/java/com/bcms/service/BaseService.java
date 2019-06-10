package com.bcms.service;

import com.bcms.common.PageableBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author Jackie
 * @className BaseService
 * @descrition 公共业务逻辑接口
 * @date 2018/12/22 15:55
 */
public interface BaseService<T extends Serializable, PK extends Serializable> {

    /**
     * 新增或修改对象
     *
     * @param entity 实体
     * @return T
     */
    T save(T entity);

    /**
     * 根据id查找对应的对象
     *
     * @param id 实体id
     * @return Optional<T>
     */
    Optional<T> findById(PK id);

    /**
     * 根据id判断对象是否存在
     *
     * @param id 实体id
     * @return boolean
     */
    boolean existsById(PK id);

    /**
     * 根据id删除相应的对象
     *
     * @param id 实体id
     */
    void deleteById(PK id);

    /**
     * 根据多个id删除相应的对象
     *
     * @param ids 多个实体id
     */
    void deleteByIds(PK[] ids);

    /**
     * 根据id集合查询相应的对象
     *
     * @param ids id集合
     * @return List<T>
     */
    List<T> findAllById(Iterable<PK> ids);


    /**
     * 查询所有对象
     *
     * @return List<T>
     */
    List<T> findAll();

    /**
     * 分页查询所有对象
     *
     * @param pageableBuilder 分页构造器
     * @return Page<T>
     */
    Page<T> findAll(PageableBuilder pageableBuilder);

    /**
     * 分页+条件查询所有对象
     *
     * @param spec            条件
     * @param pageableBuilder 分页构造器
     * @return Page<T>
     */
    Page<T> findAll(Specification<T> spec, PageableBuilder pageableBuilder);
}
