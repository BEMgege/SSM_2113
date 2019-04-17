package com.bdqn.ssm.service;

import com.bdqn.ssm.entity.Bill;

import java.util.List;

/**
 * Created by 15001 on 2019/4/10.
 */
public interface BillService {
    //分页查询订单信息，模糊查询
    public List<Bill> getBillList(String productName,
                                  Integer queryProviderId,
                                  Integer isPayment,
                                  Integer pageIndex,
                                  Integer pageSize);


    //统计账单总记录数
    public int getBillCount(String productName,
                            Integer queryProviderId,Integer isPayment);


    //添加订单信息
    public int addBill(Bill bill);

    //修改订单信息
    public void updateBill(Bill bill);

    //删除订单信息
    public int deleteBill(String bid);

    //通过ID查询订单记录
    public Bill getBillById(String bid);
}
