package com.bdqn.ssm.controller;

import com.alibaba.fastjson.JSONArray;
import com.bdqn.ssm.entity.Bill;
import com.bdqn.ssm.entity.Provider;
import com.bdqn.ssm.service.BillService;
import com.bdqn.ssm.service.ProviderService;
import com.bdqn.ssm.util.Constants;
import com.bdqn.ssm.util.PageSupport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15001 on 2019/4/10.
 */
@Controller
@RequestMapping("/bill")
public class BillController {
    @Resource
    private BillService bs;
    @Resource
    private ProviderService ps;
    @RequestMapping(value = "/billlist.html")
    public String getBillList(Model model,
                              @RequestParam(value = "queryProductName",required = false)String queryProductName,
                              @RequestParam(value = "queryProviderId",required = false)String queryProviderId,
                              @RequestParam(value = "queryIsPayment",required = false)String isPayment,
                              @RequestParam(value = "pageIndex",required = false)String pageIndex){
        Integer _queryProvideId=null;
        Integer _isPayment=null;
        int pageSize= Constants.pageSize;
        //当前页码
        int currentPageNo=1;
        if(queryProductName==null){
            queryProductName="";
        }
        if(queryProviderId!=null&&!queryProviderId.equals("")){
            _queryProvideId=Integer.parseInt(queryProviderId);
        }
        if(pageIndex!=null){
            try {
                currentPageNo=Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                return "redirect:/user/syserror.html";
            }
        }
        if(isPayment!=null&&isPayment!=""){
            _isPayment=Integer.parseInt(isPayment);
        }

        //总数量（表）
        int  totalCount=bs.getBillCount(queryProductName,_queryProvideId,_isPayment);
        //总页数
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount=pages.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo<1){
            currentPageNo=1;
        }else if(currentPageNo>totalPageCount){
            currentPageNo=totalPageCount;
        }
        List<Bill> billList=null;
        billList=bs.getBillList(queryProductName,_queryProvideId,_isPayment,currentPageNo,pageSize);
        model.addAttribute("billList",billList);
        List<Provider> providerList=null;
        providerList=ps.getProviderList();
        model.addAttribute("providerList",providerList);
        model.addAttribute("queryProductName",queryProductName);
        model.addAttribute("queryProviderId",queryProviderId);
        model.addAttribute("queryIsPayment",isPayment);
        model.addAttribute("totalPageCount",totalPageCount);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("currentPageNo",currentPageNo);
        return "billlist";
    }
    @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
    public String view(@PathVariable String id,Model model){
        Bill bill=bs.getBillById(id);
        model.addAttribute("bill",bill);
        return "billview";
    }
    @RequestMapping(value = "/delRelust.html",method = RequestMethod.GET)
    @ResponseBody
    public String delBill(@RequestParam String billid){
        Map<String,Object> map=new HashMap<String, Object>();
        int count=bs.deleteBill(billid);
        if(count>0){
            map.put("delResult","true");
        }else {
            map.put("delResult","false");
        }
        return JSONArray.toJSONString(map);
    }
    @RequestMapping(value = "/addbill.html",method = RequestMethod.GET)
    public String addBill(@ModelAttribute Bill bill){
        return "billadd";
    }
    @RequestMapping(value = "/addbill.html",method = RequestMethod.POST)
    public String insertBill(Bill bill, HttpSession session){
        bill.setModifyBy(((Bill)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        bill.setCreationDate(new Date());
        if(bs.addBill(bill)>0){
            return "redirect:/bill/billlist.html";
        }
        return "billadd";
    }
}
