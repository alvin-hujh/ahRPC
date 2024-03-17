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
    public User findById(Integer userId, String userName) {
        return new User(userId, userName);
    }

    @Override
    public int getId(int id) {
        return id;
    }

    @Override
    public String getName() {
        return "alvin";
    }

    @Override
    public String getName(int id) {
        return id + "hjh";
    }

    @Override
    public Long getLongId(long id) {
        return id;
    }

    @Override
    public String getUserName(User user) {
        return user.getName();
    }

    @Override
    public int[] getIds() {
        return new int[]{1, 2, 3};
    }

    @Override
    public long[] getLongIds() {
        return new long[]{100L, 200L, 300L};
    }

    @Override
    public int[] getIds(int[] ids) {
        return ids;
    }
}
