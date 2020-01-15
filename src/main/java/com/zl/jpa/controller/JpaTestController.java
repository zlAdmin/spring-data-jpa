package com.zl.jpa.controller;

import com.zl.jpa.model.User;
import com.zl.jpa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhanglei
 * @ProjectName: jpa
 * @create 2020-01-15 15:42
 * @Version: 1.0
 * <p>Copyright: Copyright (acmtc) 2020</p>
 **/
@RestController
@RequestMapping("/zl")
@Slf4j
public class JpaTestController {

    @Resource
    private UserRepository userRepository;

    @GetMapping("/add")
    public void addUser() {
        Date data = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(data);
        userRepository.save(new User("bb","aa123456","aa@126.com","aa",formattedDate));
    }
}
