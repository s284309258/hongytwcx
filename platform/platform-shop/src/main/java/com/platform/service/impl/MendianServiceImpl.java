package com.platform.service.impl;

import com.platform.dao.Sqlca;
import com.platform.entity.MendianEntity;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class MendianServiceImpl {

    @Autowired
    private DataSource dataSource;

    public R queryList(Map<String, Object> map) throws Exception{
        //查询列表数据
        Query query = new Query(map);

        List<Object> MendianList = Sqlca.getArrayListFromObj("select id,mendian_name,mendian_address,mendian_tel," +
                "mendian_bustime from mendian",MendianEntity.class,dataSource.getConnection());

        int total = MendianList.size();

        PageUtils pageUtil = new PageUtils(MendianList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    public R save(MendianEntity mendianEntity) throws Exception {
        Sqlca.updateObject(dataSource.getConnection(),"insert into mendian(mendian_name,mendian_address,mendian_tel,mendian_bustime) values(" +
                "?,?,?,?)",new String[]{mendianEntity.mendian_name,mendianEntity.mendian_address,mendianEntity.mendian_tel,mendianEntity.mendian_bustime});
        return R.ok();
    }

    public R info(int id) throws Exception{
        List<Object> MendianList = Sqlca.getArrayListFromObj("select id,mendian_name,mendian_address,mendian_tel," +
                "mendian_bustime from mendian where id="+id,MendianEntity.class,dataSource.getConnection());
        return R.ok().put("MendianEntity", MendianList.get(0));
    }

    public R update(MendianEntity mendianEntity) throws Exception {
        Sqlca.updateObject(dataSource.getConnection(),"update mendian set mendian_name=?,mendian_address=?," +
                "mendian_tel=?,mendian_bustime=? where id="+mendianEntity.id,new String[]{mendianEntity.mendian_name,mendianEntity.mendian_address,
                mendianEntity.mendian_tel,mendianEntity.mendian_bustime});
        return R.ok();
    }

    public int deleteBatch(Integer[]ids)  throws Exception{
        for(Integer id : ids){
            Sqlca.updateObject(dataSource.getConnection(),"delete from mendian where id="+id,new String[]{});
        }
        return 0;
    }
}
