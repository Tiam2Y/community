package top.nizy.community.strategy;

/**
 * @Classname OAuthLoginStrategy
 * @Description 授权登陆的策略
 * @Date 2021/7/14 22:14
 * @Created by NZY271
 */
public interface OAuthLoginStrategy {
    //每一个策略需要实现的方法
    LoginUserInfo getUser(String code, String state);
    String getSupportedType();  //表明支持哪些策略 -- 每个策略返回自己的策略名
}
