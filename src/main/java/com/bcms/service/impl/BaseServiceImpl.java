package com.bcms.service.impl;

import com.bcms.common.PageableBuilder;
import com.bcms.repository.BaseRepository;
import com.bcms.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @className BaseServiceImpl
 * @descrition 公共业务逻辑接口抽象实现类，专门用于继承
 * @author Jackie
 * @date 2018/12/22 15:56
 */
@Transactional(rollbackFor = Exception.class)
public abstract class BaseServiceImpl<T extends Serializable, PK extends Serializable> implements BaseService<T, PK> {

    private BaseRepository<T, PK> baseRepository;
    // private Class<T> clazz;

    // @SuppressWarnings("unchecked")

//    /**
//     * 构造函数
//     *
//     * @param baseRepository 公共数据访问接口
//     */
//    public BaseServiceImpl(BaseRepository<T, PK> baseRepository) {
//        this.baseRepository = baseRepository;
////        // 得到泛型化的超类（返回值为参数化类型，即泛型）
////        ParameterizedType pType = (ParameterizedType) this.getClass().getGenericSuperclass();
////        // 获取泛型中的第一个参数
////        this.clazz = (Class<T>) pType.getActualTypeArguments()[0];
//    }

    /**
     * 新增或修改对象
     *
     * @param entity 实体
     * @return T
     */
    @Override
    public T save(T entity) {
        return baseRepository.save(entity);
    }

    /**
     * 根据id查找对应的对象
     *
     * @param id 实体id
     * @return Optional<T>
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Optional<T> findById(PK id) {
        return baseRepository.findById(id);
    }

    /**
     * 根据id判断对象是否存在
     *
     * @param id 实体id
     * @return boolean
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean existsById(PK id) {
        return baseRepository.existsById(id);
    }

    /**
     * 根据id删除相应的对象
     *
     * @param id 实体id
     */
    @Override
    public void deleteById(PK id) {
        baseRepository.deleteById(id);
    }

    /**
     * 根据多个id删除相应的对象
     *
     * @param ids 实体id
     */
    @Override
    public void deleteByIds(PK[] ids) {
        baseRepository.deleteByIds(ids);
    }

    /**
     * 根据id集合查询相应的对象
     *
     * @param ids id集合
     * @return List<T>
     */
    @Override
    public List<T> findAllById(Iterable<PK> ids) {
        return baseRepository.findAllById(ids);
    }

    /**
     * 查询所有对象
     *
     * @return List<T>
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    /**
     * 分页查询所有对象
     *
     * @param pageableBuilder 分页构造器
     * @return Page<T>
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Page<T> findAll(PageableBuilder pageableBuilder) {
        return baseRepository.findAll(pageableBuilder.getPageable());
    }

    /**
     * 分页+条件查询所有对象
     *
     * @param spec            条件
     * @param pageableBuilder 分页构造器
     * @return Page<T>
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Page<T> findAll(Specification<T> spec, PageableBuilder pageableBuilder) {
        return baseRepository.findAll(spec, pageableBuilder.getPageable());
    }

    public void setBaseRepository(BaseRepository<T, PK> baseRepository) {
        this.baseRepository = baseRepository;
    }
}
