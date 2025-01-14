
package com.px.business.service;

import java.util.List;

import com.github.yulichang.base.MPJBaseService;
import com.px.business.domain.BusOrderTimeout;

/**
 * 超时占用表Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IBusOrderTimeoutService extends MPJBaseService<BusOrderTimeout>
{
    /**
     * 查询超时占用表列表
     *
     * @param busOrderTimeout 超时占用表
     * @return 超时占用表集合
     */
    public List<BusOrderTimeout> selectBusOrderTimeoutList(BusOrderTimeout busOrderTimeout);
}
