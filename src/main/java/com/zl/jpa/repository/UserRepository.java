package com.zl.jpa.repository;

import com.zl.jpa.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zhanglei
 * @ProjectName: jpa
 * @create 2019-11-22 13:49
 * @Version: 1.0
 * <p>Copyright: Copyright (acmtc) 2019</p>
 **/
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUserNameOrEmail(String userName, String email);

    User findByUserName(String userName);

    List<User> findByNickName(String nickName);

    Page<User> findByUserName(String userName, Pageable pageable);

}
