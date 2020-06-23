package com.platform.service;

import com.platform.entity.MendianEntity;

import java.util.List;
import java.util.Map;

public interface MendianService {
    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    MendianEntity queryObject(Integer id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<MendianEntity> queryList(Map<String, Object> map);

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存实体
     *
     * @param attributeCategory 实体
     * @return 保存条数
     */
    int save(MendianEntity attributeCategory);

    /**
     * 根据主键更新实体
     *
     * @param attributeCategory 实体
     * @return 更新条数
     */
    int update(MendianEntity attributeCategory);

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    int delete(Integer id);
}
