package xyz.herther.Test;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.io.Resources;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import xyz.herther.dto.TopicDto;
import xyz.herther.entity.Replay;
import xyz.herther.entity.Topic;
import xyz.herther.utils.mybatisUtils;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.util.*;

/**
 * Mybatis测试类
 */
public class MybatisTest {
    @Test
    public void TestSqlSessionFactory() throws IOException {
        //利用Reader加载ClassPath下的的Mybatis-config核心配置文件
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        //初始化加载SqlSessionFactory对象，同时解析Mybatis.config.xml文件
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        System.out.println("sqlsessionFactory初始化加载成功");
        SqlSession sqlSession=null;
        try {
            //创建SqlSession对象，sqlsesion是JDBC的拓展类，与数据库交互的
            sqlSession = sqlSessionFactory.openSession();
            //数据库测试连接
            Connection connection = sqlSession.getConnection();
            System.out.println(connection);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (sqlSession !=null){
                /**
                 * 如果配置文件中 dataSource type="POOLED"  POOLED代表使用数据库连接池 close则将连接池回收连接池中。
                 * 如果配置文件中 dataSource type="UNPOOLED"  UNPOOLED代表不使用数据库连接池 close则是，
                 *  调用 connection.close()方法关闭
                 */
                sqlSession.close();
            }
        }
    }

    /**
     * MybatisUtils工具类测试
     */
    @Test
    public void TestMybatisUtils(){
        //初始化
        SqlSession sqlSession =null;
        Connection connection =null;
        try {
            sqlSession = mybatisUtils.OpenSession();
            connection= sqlSession.getConnection();
            System.out.println(connection);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            mybatisUtils.Close(sqlSession);
        }
    }
    /**
     * sql映射查询topic表中的数据用例
     */
    @Test
    public void TestSelectAll(){
        SqlSession sqlSession =null;
        try {
            sqlSession = mybatisUtils.OpenSession();
            //selectAll   为xml中的id  topic 为mapper 标签中的namespace
            List<Topic> list = sqlSession.selectList("topic.selectAll");
            for (Topic i: list) {
                System.out.println(i.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mybatisUtils.Close(sqlSession);
        }
    }
    /**
     * 通过传入id 来查询
     */
    @Test
    public void TestSelectById(){
        SqlSession sqlSession =null;
        try {
            sqlSession = mybatisUtils.OpenSession();
            //因为只查询出来一条所以用selectOne ，第一个为 对应的sql 语句， 第二给为传进去的参数
            Topic t = sqlSession.selectOne("topic.selectById",2);
            System.out.println(t.toString());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mybatisUtils.Close(sqlSession);
        }
    }
    /**
     * 通过传进limit page  min max 参数来查询
     */
    @Test
    public void TestselectClick(){
        SqlSession sqlSession =null;
        try {
            sqlSession = mybatisUtils.OpenSession();
            Map map =new HashMap();
            map.put("min",10);
            map.put("max",70);
            map.put("page",10);
            map.put("limit",0);
            List<Topic> topic1 = sqlSession.selectList("topic.selectClick", map);
            for (Topic t: topic1){
                System.out.println(t.getTitle());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mybatisUtils.Close(sqlSession);
        }
    }
    /**
     * 多表查询
      */
    @Test
    public void TestselectMap(){
        SqlSession sqlSession =null;
        try {
            sqlSession = mybatisUtils.OpenSession();
            List<Map> list = sqlSession.selectList("topic.selectMap");
            for (Map  map: list){
                System.out.println(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mybatisUtils.Close(sqlSession);
        }
    }
    /**
     * 结果集映射dto
     */
    @Test
    public void TestSelectDto(){
        SqlSession sqlSession =null;
        try {
            sqlSession = mybatisUtils.OpenSession();
            List<TopicDto> topic = sqlSession.selectList("topic.selectDto");
            for (TopicDto  i: topic){
                System.out.println(i.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mybatisUtils.Close(sqlSession);
        }
    }
    /**
     * 新增数据操作
     */
    @Test
    public void TestInsert(){
        SqlSession sqlSession =null;
        try {
            sqlSession=mybatisUtils.OpenSession();
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
    /**
     * 更新操作
     */
    @Test
    public void TestUpdate(){
        SqlSession sqlSession=null;
        Topic a = new Topic();

        try {
            sqlSession = mybatisUtils.OpenSession();
            //先去原来的查询数据
            Topic topic = sqlSession.selectOne("topic.selectById", 4);
            //设置更新的内容
            topic.setTitle("测试更新");
            //执行更新
            int num = sqlSession.update("topic.updateTopic", topic);
            //提交事务
            sqlSession.commit();
            System.out.println(num);

        }catch (Exception e){
            //出现问题，数据回滚
            if(sqlSession !=null){
                sqlSession.rollback();
            }
            e.printStackTrace();
        }finally {
            //关闭资源
            mybatisUtils.Close(sqlSession);
        }

    }
    /**
     * 删除操作
     */
    @Test
    public  void TestDelete(){
        SqlSession sqlSession=null;
        try {
            sqlSession = mybatisUtils.OpenSession();
            //删除操作
            int num = sqlSession.delete("topic.deleteTopic",32);
            //提交事务
            sqlSession.commit();
            System.out.println(num);

        }catch (Exception e){
            //出现问题，数据回滚
            if(sqlSession !=null){
                sqlSession.rollback();
            }
            e.printStackTrace();
        }finally {
            //关闭资源
            mybatisUtils.Close(sqlSession);
        }
    }
    /**
     * 动态查询
     */
    @Test
    public  void TestDynamiSelect(){
        SqlSession sqlSession=null;
        try {
            sqlSession = mybatisUtils.OpenSession();
            Map map =new HashMap();
            map.put("userId",4);
            map.put("click",44);
            List<Topic> topic = sqlSession.selectList("topic.selecetDynamic", map);
            for (Topic t:topic) {
                System.out.println(t.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            mybatisUtils.Close(sqlSession);
        }
    }

    /**
     * 级联查询 一对多
     */
    @Test
    public  void TestSelectOneTopic(){
        SqlSession sqlSession=null;
        try {
            sqlSession = mybatisUtils.OpenSession();
            List<Topic> topic = sqlSession.selectList("topic.SelectOneMarny");
            for (Topic t:topic) {
                System.out.println(t.getTitle());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            mybatisUtils.Close(sqlSession);
        }
    }
    /**
     * 级联查询 多对一
     */
    @Test
    public  void TestSelectManayTopic(){
        SqlSession sqlSession=null;
        try {
            sqlSession = mybatisUtils.OpenSession();
            List<Replay> replay = sqlSession.selectList("reply.selectManyToOne");
            for (Replay t:replay) {
                System.out.println(t);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            mybatisUtils.Close(sqlSession);
        }
    }
    /**
     * 分页pagehelper
     */
    @Test
    public  void TestSelectPageHelp(){
        SqlSession sqlSession=null;
        try {
            sqlSession = mybatisUtils.OpenSession();
            //分页，从第二页开始查询，一页五条
            PageHelper.startPage(2,5);
            //返回page集合
            Page<Topic> page = (Page)sqlSession.selectList("topic.SelectPageHelp");
            System.out.println("总页数："+page.getPages());
            System.out.println("总记录数："+page.getTotal());
            System.out.println("开始行号:"+page.getStartRow());
            System.out.println("结束行号："+page.getEndRow());
            System.out.println("当前页码："+page.getPageNum());
            List<Topic> result = page.getResult();
            for (Topic i: result) {
                System.out.println("标题："+i.getTitle());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            mybatisUtils.Close(sqlSession);
        }
    }

    /**
     * 批量新增数据操作
     */
    @Test
    public void TestbathInsert(){
        SqlSession sqlSession =null;
        try {
            sqlSession=mybatisUtils.OpenSession();
            long Startime = new Date().getTime();
            ArrayList list = new ArrayList();
            for (int i=0; i<1000; i++){
                Topic topic = new Topic();
                topic.setClick(44);
                topic.setContent("测试内容"+i);
                topic.setCreateTime(new Date());
                topic.setTabId(1);
                topic.setTitle("测试标题"+i);
                topic.setUpdateTime(new Date());
                topic.setUserId(4);
                list.add(topic);
            }
            //返回影响的行数
            int num = sqlSession.insert("topic.batchInsert", list);
            //提交事务
            sqlSession.commit();
            long Endtime = new Date().getTime();
            System.out.println("成功插入："+num+"条,所用时间："+(Startime-Endtime)+"毫秒");
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
     * 批量删除数据操作
     */
    @Test
    public void TestbatchDelete(){
        SqlSession sqlSession =null;
        try {
            sqlSession=mybatisUtils.OpenSession();
            long Startime = new Date().getTime();
            ArrayList list = new ArrayList();
            for (int i=0; i<1000; i++){
                list.add("测试标题"+i);
            }
            //返回影响的行数
            int num = sqlSession.insert("topic.batchDelete", list);
            //提交事务
            sqlSession.commit();
            long Endtime = new Date().getTime();
            System.out.println("成功删除："+num+"条,所用时间："+(Startime-Endtime)+"毫秒");
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
