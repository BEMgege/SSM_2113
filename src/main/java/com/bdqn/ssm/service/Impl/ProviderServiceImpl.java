package com.bdqn.ssm.service.Impl;

import com.bdqn.ssm.dao.ProviderDao;
import com.bdqn.ssm.entity.Provider;
import com.bdqn.ssm.service.ProviderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 15001 on 2019/4/10.
 */
@Service
public class ProviderServiceImpl implements ProviderService {
    @Resource
    private ProviderDao pd;
    @Override
    public List<Provider> getProviderList() {
        return pd.getProviderList();
    }
}
