package cn.hjh.ahrpc.demo.provider;

import cn.hjh.ahrpc.core.annotation.AHProvider;
import cn.hjh.ahrpc.demo.api.User;
import cn.hjh.ahrpc.demo.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AHProvider
public class UserServiceImpl implements UserService {

    @Autowired
    Environment environment;

    @Override
    public User findById(Integer userId) {
        return new User(userId, "alvin--" + environment.getProperty("server.port") + "--" + System.currentTimeMillis());
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

    @Override
    public List<User> getList(List<User> userList) {
        return userList;
    }

    @Override
    public Map<String, User> getMap(Map<String, User> userMap) {
        return userMap;
    }

    @Override
    public Boolean getFlag(boolean flag) {
        return flag;
    }
}
