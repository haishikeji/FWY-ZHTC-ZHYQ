package com.px.business.service.impl;

import java.util.List;

import com.github.yulichang.base.MPJBaseServiceImpl;
        import com.px.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.business.mapper.BusPaymentOrderDetailMapper;
import com.px.business.domain.BusPaymentOrderDetail;
import com.px.business.service.IBusPaymentOrderDetailService;

/**
 * 支付订单明细Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class BusPaymentOrderDetailServiceImpl extends MPJBaseServiceImpl<BusPaymentOrderDetailMapper, BusPaymentOrderDetail> implements IBusPaymentOrderDetailService
{
    @Autowired
    private BusPaymentOrderDetailMapper busPaymentOrderDetailMapper;

    /**
     * 查询支付订单明细
     *
     * @param paymentDetailId 支付订单明细主键
     * @return 支付订单明细
     */
    @Override
    public BusPaymentOrderDetail selectBusPaymentOrderDetailByPaymentDetailId(Long paymentDetailId)
    {
        return busPaymentOrderDetailMapper.selectBusPaymentOrderDetailByPaymentDetailId(paymentDetailId);
    }

    /**
     * 查询支付订单明细列表
     *
     * @param busPaymentOrderDetail 支付订单明细
     * @return 支付订单明细
     */
    @Override
    public List<BusPaymentOrderDetail> selectBusPaymentOrderDetailList(BusPaymentOrderDetail busPaymentOrderDetail)
    {
        return busPaymentOrderDetailMapper.selectBusPaymentOrderDetailList(busPaymentOrderDetail);
    }

    /**
     * 新增支付订单明细
     *
     * @param busPaymentOrderDetail 支付订单明细
     * @return 结果
     */
    @Override
    public int insertBusPaymentOrderDetail(BusPaymentOrderDetail busPaymentOrderDetail)
    {
                busPaymentOrderDetail.setCreateTime(DateUtils.getNowDate());
            return busPaymentOrderDetailMapper.insertBusPaymentOrderDetail(busPaymentOrderDetail);
    }

    /**
     * 修改支付订单明细
     *
     * @param busPaymentOrderDetail 支付订单明细
     * @return 结果
     */
    @Override
    public int updateBusPaymentOrderDetail(BusPaymentOrderDetail busPaymentOrderDetail)
    {
                busPaymentOrderDetail.setUpdateTime(DateUtils.getNowDate());
        return busPaymentOrderDetailMapper.updateBusPaymentOrderDetail(busPaymentOrderDetail);
    }

    /**
     * 批量删除支付订单明细
     *
     * @param paymentDetailIds 需要删除的支付订单明细主键
     * @return 结果
     */
    @Override
    public int deleteBusPaymentOrderDetailByPaymentDetailIds(Long[] paymentDetailIds)
    {
        return busPaymentOrderDetailMapper.deleteBusPaymentOrderDetailByPaymentDetailIds(paymentDetailIds);
    }

    /**
     * 删除支付订单明细信息
     *
     * @param paymentDetailId 支付订单明细主键
     * @return 结果
     */
    @Override
    public int deleteBusPaymentOrderDetailByPaymentDetailId(Long paymentDetailId)
    {
        return busPaymentOrderDetailMapper.deleteBusPaymentOrderDetailByPaymentDetailId(paymentDetailId);
    }
}
