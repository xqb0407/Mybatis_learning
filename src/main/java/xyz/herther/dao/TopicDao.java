package xyz.herther.dao;

import org.apache.ibatis.annotations.*;
import xyz.herther.dto.TopicDto;
import xyz.herther.entity.Topic;

import java.util.List;

public interface TopicDao {

    @Select("select * from topic limit 0,#{limit}") //语句中写入与mapper.xml一样的sql语句
    List<Topic> SelectAllLimit(@Param("limit") int limit); //@Param 对应sql语句中的传入参数

    @Insert("INSERT INTO topic(user_id,create_time,update_time,title,content,click,tab_id) VALUES (#{userId},#{createTime},#{updateTime},#{title},#{content},#{click},#{tabId})")
    @SelectKey(statement = "SELECT last_insert_id()" ,resultType = Integer.class, keyProperty = "id",before = false)
    //statement 查询id 的语句  resultType返回的类型   keyProperty返回对应实体类中那个字段， before true 在语句插入语句之前 false 在语句之后
    int InsertNot(Topic topic);
    @Select("select t.* ,tab.tab_name from topic t join tab on t.tab_id= tab.id")
    @Results({
            @Result(column = "id",property = "id" , id= true),
            @Result(column="user_id",property="topic.userId"),
            @Result(column="create_time",property="topic.createTime"),
            @Result(property="topic.updateTime" ,column="update_time"),
            @Result(property="topic.title" ,column="title"),
            @Result(property="topic.content" ,column="content"),
            @Result(property="topic.click" ,column="click"),
            @Result(property="topic.tabId", column="tabId"),
            @Result(property="tabName", column="tab_name"),
            @Result(property="tabNameEn", column="tab_name_en")
    })
    List<TopicDto> SelectTopicDto();
}
