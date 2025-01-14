package com.px.business.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.px.bigdata.domain.IncomeSearch;
import com.px.bigdata.domain.MonthlyIncomeGroupItemVo;
import com.px.bigdata.domain.OperatorReportSearchVO;
import com.px.business.domain.vo.OrderPaymentWaterVO;
import com.px.common.core.domain.entity.BusParkArea;
import com.px.common.utils.DateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.business.mapper.BusMonthlyOrderInfoMapper;
import com.px.business.domain.BusMonthlyOrderInfo;
import com.px.business.service.IBusMonthlyOrderInfoService;

/**
 * 包月订单Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class BusMonthlyOrderInfoServiceImpl extends MPJBaseServiceImpl<BusMonthlyOrderInfoMapper, BusMonthlyOrderInfo> implements IBusMonthlyOrderInfoService {

    @Autowired
    private BusMonthlyOrderInfoMapper busMonthlyOrderInfoMapper;

    /**
     * 查询包月订单
     *
     * @param monthlyOrderId 包月订单主键
     * @return 包月订单
     */
    @Override
    public BusMonthlyOrderInfo selectBusMonthlyOrderInfoByMonthlyOrderId(Long monthlyOrderId) {
        return busMonthlyOrderInfoMapper.selectBusMonthlyOrderInfoByMonthlyOrderId(monthlyOrderId);
    }

    /**
     * 查询包月订单列表
     *
     * @param busMonthlyOrderInfo 包月订单
     * @return 包月订单
     */
    @Override
    public List<BusMonthlyOrderInfo> selectBusMonthlyOrderInfoList(BusMonthlyOrderInfo busMonthlyOrderInfo) {
        return busMonthlyOrderInfoMapper.selectBusMonthlyOrderInfoList(busMonthlyOrderInfo);
    }

    /**
     * 新增包月订单
     *
     * @param busMonthlyOrderInfo 包月订单
     * @return 结果
     */
    @Override
    public int insertBusMonthlyOrderInfo(BusMonthlyOrderInfo busMonthlyOrderInfo) {
        busMonthlyOrderInfo.setCreateTime(DateUtils.getNowDate());
        return busMonthlyOrderInfoMapper.insertBusMonthlyOrderInfo(busMonthlyOrderInfo);
    }

    /**
     * 修改包月订单
     *
     * @param busMonthlyOrderInfo 包月订单
     * @return 结果
     */
    @Override
    public int updateBusMonthlyOrderInfo(BusMonthlyOrderInfo busMonthlyOrderInfo) {
        busMonthlyOrderInfo.setUpdateTime(DateUtils.getNowDate());
        return busMonthlyOrderInfoMapper.updateBusMonthlyOrderInfo(busMonthlyOrderInfo);
    }

    /**
     * 批量删除包月订单
     *
     * @param monthlyOrderIds 需要删除的包月订单主键
     * @return 结果
     */
    @Override
    public int deleteBusMonthlyOrderInfoByMonthlyOrderIds(Long[] monthlyOrderIds) {
        return busMonthlyOrderInfoMapper.deleteBusMonthlyOrderInfoByMonthlyOrderIds(monthlyOrderIds);
    }

    /**
     * 删除包月订单信息
     *
     * @param monthlyOrderId 包月订单主键
     * @return 结果
     */
    @Override
    public int deleteBusMonthlyOrderInfoByMonthlyOrderId(Long monthlyOrderId) {
        return busMonthlyOrderInfoMapper.deleteBusMonthlyOrderInfoByMonthlyOrderId(monthlyOrderId);
    }

    @Override
    public BusMonthlyOrderInfo selectBusMonthlyOrderInfoByMonthlyOrderSn(String orderSn) {
        return busMonthlyOrderInfoMapper.selectBusMonthlyOrderInfoByMonthlyOrderSn(orderSn);
    }

    @Override
    public List<OrderPaymentWaterVO> selectMonthlyOrderPaymentWaterList(OrderPaymentWaterVO orderPaymentWaterVO) {
        return busMonthlyOrderInfoMapper.selectMonthlyOrderPaymentWaterList(orderPaymentWaterVO);
    }

    @Override
    public List<MonthlyIncomeGroupItemVo> selectMonthlyIncomeGroupItemVos(IncomeSearch incomeSearch) {
        MPJLambdaWrapper<BusMonthlyOrderInfo> wrapper = new MPJLambdaWrapper<BusMonthlyOrderInfo>()
                .selectSum(BusMonthlyOrderInfo::getOrderRealityAmount, MonthlyIncomeGroupItemVo::getTotalAmount)
                .selectAs(BusMonthlyOrderInfo::getPaymentType, MonthlyIncomeGroupItemVo::getPaymentType)
                .innerJoin(BusParkArea.class, BusParkArea::getParkAreaId, BusMonthlyOrderInfo::getParkAreaId)
                .eq(BusMonthlyOrderInfo::getPaymentStatus, 2)
                .groupBy(BusMonthlyOrderInfo::getPaymentType);

        if (ObjectUtils.isNotEmpty(incomeSearch.getPCode())) {
            wrapper.eq(BusParkArea::getPCode, incomeSearch.getPCode());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getCityCode())) {
            wrapper.eq(BusParkArea::getPCode, incomeSearch.getCityCode());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getCountyCode())) {
            wrapper.eq(BusParkArea::getPCode, incomeSearch.getCountyCode());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getParkAreaId())) {
            wrapper.eq(BusMonthlyOrderInfo::getParkAreaId, incomeSearch.getParkAreaId());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getBeginTime())) {
            wrapper.gt(BusMonthlyOrderInfo::getPaymentTime, incomeSearch.getBeginTime());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getEndTime())) {
            wrapper.gt(BusMonthlyOrderInfo::getPaymentTime, incomeSearch.getEndTime());
        }

        return selectJoinList(MonthlyIncomeGroupItemVo.class, wrapper);
    }


    @Override
    public Long selectMonthlyIncomeGroupCount(IncomeSearch incomeSearch) {
        MPJLambdaWrapper<BusMonthlyOrderInfo> wrapper = new MPJLambdaWrapper<BusMonthlyOrderInfo>()
                .innerJoin(BusParkArea.class, BusParkArea::getParkAreaId, BusMonthlyOrderInfo::getParkAreaId)
                .eq(BusMonthlyOrderInfo::getPaymentStatus, 2);

        if (ObjectUtils.isNotEmpty(incomeSearch.getPCode())) {
            wrapper.eq(BusParkArea::getPCode, incomeSearch.getPCode());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getCityCode())) {
            wrapper.eq(BusParkArea::getPCode, incomeSearch.getCityCode());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getCountyCode())) {
            wrapper.eq(BusParkArea::getPCode, incomeSearch.getCountyCode());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getParkAreaId())) {
            wrapper.eq(BusMonthlyOrderInfo::getParkAreaId, incomeSearch.getParkAreaId());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getBeginTime())) {
            wrapper.gt(BusMonthlyOrderInfo::getPaymentTime, incomeSearch.getBeginTime());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getEndTime())) {
            wrapper.gt(BusMonthlyOrderInfo::getPaymentTime, incomeSearch.getEndTime());
        }

        return selectJoinCount(wrapper);
    }


    @Override
    public List<Map<String, Object>> fetchOrderAmountsGroupByCreateAt(IncomeSearch incomeSearch) {
        MPJLambdaWrapper<BusMonthlyOrderInfo> wrapper = new MPJLambdaWrapper<BusMonthlyOrderInfo>("boi")
                .selectSum(BusMonthlyOrderInfo::getOrderRealityAmount)
                .select("DATE_FORMAT(boi.create_time,'%Y-%m-%d') as fmt_create_time")
                //                .eq(BusMonthlyOrderInfo::getStatus, 2)
                .groupBy("DATE_FORMAT(boi.create_time,'%Y-%m-%d')");

        if (ObjectUtils.isNotEmpty(incomeSearch.getBeginTime())) {
            wrapper.ge(BusMonthlyOrderInfo::getCreateTime, incomeSearch.getBeginTime());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getEndTime())) {
            wrapper.le(BusMonthlyOrderInfo::getCreateTime, incomeSearch.getEndTime());
        }
        return selectJoinMaps(wrapper);
    }

    @Override
    public List<Map<String, Object>> fetchOrderNumbersGroupByCreateAt(IncomeSearch incomeSearch) {
        MPJLambdaWrapper<BusMonthlyOrderInfo> wrapper = new MPJLambdaWrapper<BusMonthlyOrderInfo>("boi")
                .selectCount(BusMonthlyOrderInfo::getMonthlyOrderId)
                .select("DATE_FORMAT(boi.create_time,'%Y-%m-%d') as fmt_create_time")
//                .eq(BusMonthlyOrderInfo::getStatus, 2)
                .groupBy("DATE_FORMAT(boi.create_time,'%Y-%m-%d')");

        if (ObjectUtils.isNotEmpty(incomeSearch.getBeginTime())) {
            wrapper.ge(BusMonthlyOrderInfo::getCreateTime, incomeSearch.getBeginTime());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getEndTime())) {
            wrapper.le(BusMonthlyOrderInfo::getCreateTime, incomeSearch.getEndTime());
        }
        return selectJoinMaps(wrapper);
    }

    @Override
    public BigDecimal countTotalAmount(IncomeSearch incomeSearch) {
        MPJLambdaWrapper<BusMonthlyOrderInfo> wrapper = new MPJLambdaWrapper<BusMonthlyOrderInfo>("boi")
                .selectSum(BusMonthlyOrderInfo::getOrderRealityAmount, BusMonthlyOrderInfo::getOrderRealityAmount)
                .eq(BusMonthlyOrderInfo::getPaymentStatus, 2);
        if (ObjectUtils.isNotEmpty(incomeSearch.getBeginTime())) {
            wrapper.ge(BusMonthlyOrderInfo::getCreateTime, incomeSearch.getBeginTime());
        }
        if (ObjectUtils.isNotEmpty(incomeSearch.getEndTime())) {
            wrapper.le(BusMonthlyOrderInfo::getCreateTime, incomeSearch.getEndTime());
        }
        BusMonthlyOrderInfo busMonthlyOrderInfo = selectJoinOne(BusMonthlyOrderInfo.class, wrapper);
        if (ObjectUtils.isEmpty(busMonthlyOrderInfo)) {
            return BigDecimal.valueOf(0);
        }
        return busMonthlyOrderInfo.getOrderRealityAmount();
    }

    @Override
    public BigDecimal fetchByOperatorReport(OperatorReportSearchVO searchVO) {
        MPJLambdaWrapper<BusMonthlyOrderInfo> wrapper = new MPJLambdaWrapper<BusMonthlyOrderInfo>("boi")
                .selectSum(BusMonthlyOrderInfo::getOrderRealityAmount, BusMonthlyOrderInfo::getOrderRealityAmount);

        if (ObjectUtils.isNotEmpty(searchVO.getBeginTime())) {
            wrapper.ge(BusMonthlyOrderInfo::getCreateTime, searchVO.getBeginTime());
        }
        if (ObjectUtils.isNotEmpty(searchVO.getEndTime())) {
            wrapper.le(BusMonthlyOrderInfo::getCreateTime, searchVO.getEndTime());
        }
        if (ObjectUtils.isNotEmpty(searchVO.getPaymentStatus())) {
            wrapper.ge(BusMonthlyOrderInfo::getPaymentStatus, searchVO.getPaymentStatus());
        }
        BusMonthlyOrderInfo info = selectJoinOne(BusMonthlyOrderInfo.class, wrapper);

        if (ObjectUtils.isEmpty(info)) {
            return BigDecimal.valueOf(0);
        }
        return info.getOrderRealityAmount();
    }


}
