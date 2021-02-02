package xyz.herther.utils;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


import java.io.IOException;
import java.io.Reader;

/**
 * MybatisUtil 是Mybatis工具类，保证SqlSessionFactory保证全局唯一
 */
public class mybatisUtils {
    //利用static 静态，它属于类，而不属于对象
    private static SqlSessionFactory sqlSessionFactory =null;
    //利用静态代码块，实例化类sqlSessionFactory
    static {
        Reader reader=null;
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e);
            //向上抛出初始化ExceptionInInitializerError异常 向调用者通知
            throw new ExceptionInInitializerError(e);
        }
    }
    //创建SqlSession对象，sqlsesion是JDBC的拓展类，与数据库交互的
    public static SqlSession OpenSession(){
        return sqlSessionFactory.openSession();
    }
    //关闭资源
    public static void Close(SqlSession sqlSession){
        if (sqlSession !=null){
            sqlSession.close();
        }

    }
}
