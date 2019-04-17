package com.bdqn.ssm.dao;

import com.bdqn.ssm.entity.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 15001 on 2019/4/10.
 */
public interface BillDao {
    //分页查询订单信息，模糊查询
    public List<Bill> getBillList(@Param("productName")String productName,
                                  @Param("queryProviderId")Integer queryProviderId,
                                  @Param("isPayment")Integer ispayment,
                                  @Param("pageIndex")Integer pageIndex,
                                  @Param("pageSize")Integer pageSize);


    //统计账单总记录数
    public int getBillCount(@Param("productName") String productName,
                            @Param("queryProviderId")Integer queryProviderId,
                            @Param("isPayment")Integer isPayment);


    //添加订单信息
    public int addBill(Bill bill);

    //修改订单信息
    public void updateBill(Bill bill);

    //删除订单信息
    public int deleteBill(String bid);

    //通过ID查询订单记录
    public Bill getBillById(String bid);

}
