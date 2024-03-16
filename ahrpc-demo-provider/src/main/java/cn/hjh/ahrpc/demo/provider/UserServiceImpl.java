package cn.hjh.ahrpc.demo.provider;

import cn.hjh.ahrpc.core.annotation.AHProvider;
import cn.hjh.ahrpc.demo.api.User;
import cn.hjh.ahrpc.demo.api.UserService;
import org.springframework.stereotype.Component;

@Component
@AHProvider
public class UserServiceImpl implements UserService {
    @Override
    public User findById(Integer userId) {

        return new User(userId, "alvin-" + System.currentTimeMillis());
    }

    @Override
    public int getId(int id) {
        return id;
    }
}
