package com.zl.jpa;

import com.zl.jpa.model.User;
import com.zl.jpa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;
import static org.springframework.data.domain.ExampleMatcher.matching;


@SpringBootTest
class JpaApplicationTests {

    @Resource
    private UserRepository userRepository;

    @Test
    public void testSave(){
        Date data = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(data);
        userRepository.save(new User("bb","aa123456","aa@126.com","aa",formattedDate));
        userRepository.save(new User("aa","aa123456","aa@126.com","aa",formattedDate));

        userRepository.delete(userRepository.findByUserName("aa"));
    }

    @Test
    public void testQuery() {
        List<User> users = userRepository.findByNickName("bb");
        assertThat(4).isEqualTo(users.size());
    }


    @Test
    public void substringMatching() {
        // 通过Example查询以ac结尾的email
        User user = new User();
        user.setEmail("ac");
        Example<User> example = Example.of(user, matching().
                withStringMatcher(ExampleMatcher.StringMatcher.ENDING));

        assertThat(userRepository.findAll(example).size()).isEqualTo(1);
    }

    /**
     * @Description 查询：email以aa@126.com开头，且nickName是AA（忽略大小写）
     * @return void
     * @throws 
     * @Author zhanglei
     * @Date 15:08 2019/11/22
     * @Param []
     */
    @Test
    public void matchStartingStringsIgnoreCase() {
        User user = new User();
        user.setEmail("aa@126.com");
        user.setNickName("AA");
        Example<User> example = Example.of(user, matching(). //
                withMatcher("email", startsWith()). //
                withMatcher("nickName", ignoreCase()));

        assertThat(userRepository.findAll(example).size()).isEqualTo(1);
    }

    @Test
    public void configuringMatchersUsingLambdas() {
        User user = new User();
        user.setEmail("aa@126.com");
        user.setNickName("AA");
        Example<User> example = Example.of(user, matching(). //
                withMatcher("email", matcher -> matcher.startsWith()). //
                withMatcher("nickName", matcher -> matcher.ignoreCase()));

        assertThat(userRepository.findAll(example).size()).isEqualTo(1);
    }

    /**
     * @Description 单表page查询
     * @return void
     * @throws 
     * @Author zhanglei
     * @Date 16:09 2019/11/22
     * @Param []
     **/
    @Test
    public void getPage() {
        Page<User> page = userRepository.findByUserName("aa", PageRequest.of(0, 2));
        System.out.println(page.getTotalPages());
    }

    @Test
    public void findOne() {
        Optional<User> optional = userRepository.findById("1");
        boolean present = optional.isPresent();
        if (present) {
            User user = optional.get();
            assertThat(user.getId().equals(1L));
        }
    }

    /**
     * @Description 原生sql查询
     * @return void
     * @throws 
     * @Author zhanglei
     * @Date 17:54 2019/12/18
     * @Param []
     **/
    @Test
    public void createSQLNativeQueryTest() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" 	u.id, ");
        sql.append(" 	u.nick_name  ");
        sql.append(" FROM ");
        sql.append(" 	`user` AS u  ");
        sql.append(" WHERE ");
        sql.append(" 	u .id = ? ");
        String id = "1";
        Query sqlNativeQuery = userRepository.createSQLNativeQuery(sql.toString(), id);
        List resultList = sqlNativeQuery.getResultList();
        for (int i = 0; i < resultList.size(); i++) {
            Object[] objs = (Object[])resultList.get(i);
            User u = new User();
            u.setId(Objects.toString(objs[0], ""));
            u.setNickName(Objects.toString(objs[1], ""));
            resultList.set(i, u);
        }
    }

}
