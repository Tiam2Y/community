package top.nizy.community.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.nizy.community.dto.AccessTokenDTO;
import top.nizy.community.provider.dto.GithubUser;
import top.nizy.community.provider.GithubProvider;

/**
 * @Classname GithubUserStrategy
 * @Description Github用户登陆时的相应策略行为
 * @Date 2021/7/15 10:53
 * @Created by NZY271
 */
@Service
public class GithubUserStrategy implements OAuthLoginStrategy {
    @Autowired
    private GithubProvider githubProvider;

    @Override
    public LoginUserInfo getUser(String code, String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        loginUserInfo.setAvatarUrl(githubUser.getAvatarUrl());
        loginUserInfo.setId(githubUser.getId());
        loginUserInfo.setName(githubUser.getName());
        return loginUserInfo;
    }

    @Override
    public String getSupportedType() {
        return "github";
    }
}
