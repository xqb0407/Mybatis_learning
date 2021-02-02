package xyz.herther.main;

import org.apache.ibatis.session.SqlSession;
import xyz.herther.entity.Topic;
import xyz.herther.utils.mybatisUtils;

import java.util.Date;

public class TestMain {
    public static void main(String[] args) {
        SqlSession sqlSession =null;
        try {
            sqlSession= mybatisUtils.OpenSession();
            Topic topic = new Topic();
            topic.setClick(44);
            topic.setContent("测试内容");
            topic.setCreateTime(new Date());
            topic.setTabId(1);
            topic.setTitle("测试标题");
            topic.setUpdateTime(new Date());
            topic.setUserId(4);
            //返回影响的行数
            int insert = sqlSession.insert("topic.inserTopic", topic);
            //提交事务
            sqlSession.commit();
            System.out.println(topic.getId());
        }catch (Exception e){
            //出现异常、事务回滚
            if(sqlSession !=null){
                sqlSession.rollback();
            }
        }finally {

            mybatisUtils.Close(sqlSession);
        }
    }
}
