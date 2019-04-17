package com.bdqn.ssm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 15001 on 2019/4/11.
 */
@Controller
@RequestMapping("/provider")
public class ProviderController extends BaseController {
    public String providerList(){
        return "providerlist";
    }
}
