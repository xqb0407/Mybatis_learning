package xyz.herther.dto;

import xyz.herther.entity.Topic;

/**
 * 数据传输对象
 */
public class TopicDto {
    private Topic topic =new Topic();
    private String tabName;
    private String tabNameEn;


    @Override
    public String toString() {
        return "TopicDto{" +
                "topic=" + topic +
                ", tabName='" + tabName + '\'' +
                ", tabNameEn='" + tabNameEn + '\'' +
                '}';
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getTabNameEn() {
        return tabNameEn;
    }

    public void setTabNameEn(String tabNameEn) {
        this.tabNameEn = tabNameEn;
    }
}
