package cn.hjh.ahrpc.demo.api;

public interface UserService {
    User findById(Integer userId);

    int getId(int id);
}
