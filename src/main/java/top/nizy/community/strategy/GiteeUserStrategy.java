package top.nizy.community.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.nizy.community.dto.AccessTokenDTO;
import top.nizy.community.provider.GiteeProvider;
import top.nizy.community.provider.dto.GiteeUser;

/**
 * @Classname GithubUserStrategy
 * @Description Github用户登陆时的相应策略行为
 * @Date 2021/7/15 10:53
 * @Created by NZY271
 */
@Service
public class GiteeUserStrategy implements OAuthLoginStrategy {
    @Autowired
    private GiteeProvider giteeProvider;

    @Override
    public LoginUserInfo getUser(String code, String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = giteeProvider.getAccessToken(accessTokenDTO);
        GiteeUser giteeUser = giteeProvider.getUser(accessToken);
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        loginUserInfo.setAvatarUrl(giteeUser.getAvatarUrl());
        loginUserInfo.setId(giteeUser.getId());
        loginUserInfo.setName(giteeUser.getName());
        return loginUserInfo;
    }

    @Override
    public String getSupportedType() {
        return "gitee";
    }
}
