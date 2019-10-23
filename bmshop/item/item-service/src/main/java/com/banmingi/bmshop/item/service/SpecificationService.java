package com.banmingi.bmshop.item.service;

import com.banmingi.bmshop.item.mapper.SpecGroupMapper;
import com.banmingi.bmshop.item.mapper.SpecParamMapper;
import com.banmingi.bmshop.item.pojo.SpecGroup;
import com.banmingi.bmshop.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @auther 半命i 2019/10/22
 * @description
 */
@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据分类id查询参数组.
     * @param cid
     * @return
     */
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup record = new SpecGroup();
        record.setCid(cid);
        return this.specGroupMapper.select(record);
    }

    /**
     * 根据条件查询规格参数.
     * @param gid
     * @return
     */
    public List<SpecParam> queryParams(Long gid) {
        SpecParam record = new SpecParam();
        record.setGroupId(gid);
        return this.specParamMapper.select(record);
    }
}
