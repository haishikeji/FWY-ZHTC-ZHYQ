
package com.px.operate.mapper;

import java.util.List;

import com.github.yulichang.base.MPJBaseMapper;
import com.px.operate.domain.OperateUserInvoice;

/**
 * 发票抬头信息Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface OperateUserInvoiceMapper extends MPJBaseMapper<OperateUserInvoice>
{
    /**
     * 查询发票抬头信息
     *
     * @param userInvoiceId 发票抬头信息主键
     * @return 发票抬头信息
     */
    public OperateUserInvoice selectOperateUserInvoiceByUserInvoiceId(Long userInvoiceId);

    /**
     * 查询发票抬头信息列表
     *
     * @param operateUserInvoice 发票抬头信息
     * @return 发票抬头信息集合
     */
    public List<OperateUserInvoice> selectOperateUserInvoiceList(OperateUserInvoice operateUserInvoice);

    /**
     * 新增发票抬头信息
     *
     * @param operateUserInvoice 发票抬头信息
     * @return 结果
     */
    public int insertOperateUserInvoice(OperateUserInvoice operateUserInvoice);

    /**
     * 修改发票抬头信息
     *
     * @param operateUserInvoice 发票抬头信息
     * @return 结果
     */
    public int updateOperateUserInvoice(OperateUserInvoice operateUserInvoice);

    /**
     * 删除发票抬头信息
     *
     * @param userInvoiceId 发票抬头信息主键
     * @return 结果
     */
    public int deleteOperateUserInvoiceByUserInvoiceId(Long userInvoiceId);

    /**
     * 批量删除发票抬头信息
     *
     * @param userInvoiceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOperateUserInvoiceByUserInvoiceIds(Long[] userInvoiceIds);

    public int updateInvoiceIsDefault(OperateUserInvoice operateUserInvoice);

    public OperateUserInvoice selectUserDefaultInvoice(Long userId);
}
