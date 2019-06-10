package com.bcms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

/**
 * @className BaseRepository
 * @descrition 公共数据访问接口
 * @author Jackie
 * @date 2018/12/21 22:41
 */
@NoRepositoryBean
public interface BaseRepository<T extends Serializable, PK extends Serializable> extends JpaRepository<T, PK>, JpaSpecificationExecutor<T> {
    /**
     * 根据多个id删除指定的对象
     *
     * @param ids 对象的id
     */
    @Modifying
    @Query("delete from #{#entityName} e where e.id in (:ids)")
    void deleteByIds(@Param("ids") PK[] ids);
}