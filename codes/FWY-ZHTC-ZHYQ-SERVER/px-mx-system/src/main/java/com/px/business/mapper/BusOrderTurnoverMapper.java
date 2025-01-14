
package com.px.business.mapper;

import java.util.List;

import com.github.yulichang.base.MPJBaseMapper;
import com.px.business.domain.BusOrderTurnover;

/**
 * 泊位周转率报表Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface BusOrderTurnoverMapper extends MPJBaseMapper<BusOrderTurnover>
{
    /**
     * 查询泊位周转率报表列表
     *
     * @param busOrderTurnover 泊位周转率报表
     * @return 泊位周转率报表集合
     */
    public List<BusOrderTurnover> selectBusOrderTurnoverList(BusOrderTurnover busOrderTurnover);

    public List<BusOrderTurnover> selectTurnoverTrendChart(BusOrderTurnover busOrderTurnover);
}
