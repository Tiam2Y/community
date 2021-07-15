package top.nizy.community.strategy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname LoginStrategyFactory
 * @Description 登陆策略的工厂类
 * @Date 2021/7/15 14:19
 * @Created by NZY271
 */
@Service
public class LoginStrategyFactory {
    @Autowired
    private List<OAuthLoginStrategy> strategies;

    public OAuthLoginStrategy getStrategy(String type) {
        for (OAuthLoginStrategy strategy : strategies) {
            if (StringUtils.equals(strategy.getSupportedType(), type))
                return strategy;
        }
        return null;
    }
}
