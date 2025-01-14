package com.px.business.service.impl;

import java.util.List;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.business.mapper.BusOrderTurnoverMapper;
import com.px.business.domain.BusOrderTurnover;
import com.px.business.service.IBusOrderTurnoverService;

/**
 * 泊位周转率报表Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class BusOrderTurnoverServiceImpl extends MPJBaseServiceImpl<BusOrderTurnoverMapper, BusOrderTurnover> implements IBusOrderTurnoverService
{
    @Autowired
    private BusOrderTurnoverMapper busOrderTurnoverMapper;

    /**
     * 查询泊位周转率报表列表
     *
     * @param busOrderTurnover 泊位周转率报表
     * @return 泊位周转率报表
     */
    @Override
    public List<BusOrderTurnover> selectBusOrderTurnoverList(BusOrderTurnover busOrderTurnover)
    {
        return busOrderTurnoverMapper.selectBusOrderTurnoverList(busOrderTurnover);
    }

    @Override
    public List<BusOrderTurnover> selectTurnoverTrendChart(BusOrderTurnover busOrderTurnover) {
        List<BusOrderTurnover> list = busOrderTurnoverMapper.selectTurnoverTrendChart(busOrderTurnover);
        return list;
    }
}
