package com.platform.dao;

import com.platform.entity.ProductVo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @gitee https://gitee.com/fuyang_lipengjun/platform
 * @date 2017-08-11 09:16:46
 */
public interface ApiProductMapper extends BaseDao<ProductVo> {
    String getSpecificationPicture(@Param("ids") List id,@Param("goods_id") String goods_id);
}
