package xyz.herther.Test;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import xyz.herther.dao.TopicDao;
import xyz.herther.dto.TopicDto;
import xyz.herther.entity.Topic;
import xyz.herther.utils.mybatisUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 注解开发的测试类
 */
public class MybatisNote {

    /**
     * 注解测试查询 limi参数
     */
    @Test
    public void TestNotSelect(){
        SqlSession sqlSession=null;
        try {
            sqlSession = mybatisUtils.OpenSession();
            //传入那个Dao (后台自动实现，TopicDao接口)
            TopicDao topicDao = sqlSession.getMapper(TopicDao.class);
            List<Topic> topics = topicDao.SelectAllLimit(10);
            //打印查询出来的长度
            System.out.println(topics.size());
        }catch (Exception E){
            E.printStackTrace();
        }finally {
            mybatisUtils.Close(sqlSession);
        }
    }

    /**
     * 注解插入
     */
    @Test
    public void TestNoteInsert(){
        SqlSession sqlSession =null;
        try {
            sqlSession=mybatisUtils.OpenSession();
            Topic topic = new Topic();
            topic.setClick(44);
            topic.setContent("测试内容eeeee");
            topic.setCreateTime(new Date());
            topic.setTabId(1);
            topic.setTitle("测试标题eeee");
            topic.setUpdateTime(new Date());
            topic.setUserId(4);
            TopicDao topicDao = sqlSession.getMapper(TopicDao.class);
            //返回影响的行数
            int num = topicDao.InsertNot(topic);
            //提交事务
            sqlSession.commit();
            System.out.println("插入后的id为："+topic.getId());
        }catch (Exception e){
            //出现异常、事务回滚
            if(sqlSession !=null){
                sqlSession.rollback();
            }
        }finally {
            mybatisUtils.Close(sqlSession);
        }
    }

    /**
     * 注解查询多表
     */
    @Test
    public void SelectTopicDto(){
        SqlSession sqlSession =null;
        try {
            sqlSession=mybatisUtils.OpenSession();

            TopicDao topicDao = sqlSession.getMapper(TopicDao.class);
            //返回影响的行数
            List<TopicDto> topicDtos = topicDao.SelectTopicDto();
            //提交事务
            sqlSession.commit();
            System.out.println("查询总长度："+topicDtos.size());
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
