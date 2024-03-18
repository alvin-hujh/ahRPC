package cn.hjh.ahrpc.demo.api;

import java.util.List;
import java.util.Map;

public interface UserService {
    User findById(Integer userId);

    User findById(Integer userId, String userName);

    int getId(int id);

    String getName();

    String getName(int id);

    Long getLongId(long id);

    String getUserName(User user);

    int[] getIds();

    long[] getLongIds();

    int[] getIds(int[] ids);

    List<User> getList(List<User> userList);

    Map<String, User> getMap(Map<String, User> userMap);

    Boolean getFlag(boolean flag);
}
