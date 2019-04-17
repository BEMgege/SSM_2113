package com.bdqn.ssm.service.Impl;

import com.bdqn.ssm.dao.BillDao;
import com.bdqn.ssm.entity.Bill;
import com.bdqn.ssm.service.BillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 15001 on 2019/4/10.
 */
@Service
public class BillServiceImpl implements BillService {
    @Resource
    private BillDao bd;
    @Override
    public List<Bill> getBillList(String productName, Integer queryProviderId,Integer isPayment, Integer pageIndex, Integer pageSize) {
        pageIndex=(pageIndex-1)*pageSize;
        return bd.getBillList(productName,queryProviderId,isPayment,pageIndex,pageSize);
    }

    @Override
    public int getBillCount(String productName, Integer queryProviderId,Integer isPayment) {
        return bd.getBillCount(productName,queryProviderId,isPayment);
    }

    @Override
    public int addBill(Bill bill) {

        return bd.addBill(bill);
    }

    @Override
    public void updateBill(Bill bill) {
      bd.updateBill(bill);
    }

    @Override
    public int  deleteBill(String bid) {

        return bd.deleteBill(bid);
    }

    @Override
    public Bill getBillById(String bid) {
        return bd.getBillById(bid);
    }
}
