package net.sh.rabbitmq.demo.dao;

import java.util.List;

import net.sh.rabbitmq.demo.entity.DemoEntity;
import net.sh.rabbitmq.demo.entity.DemoExample;
import org.apache.ibatis.annotations.Param;

public interface DemoMapper {

    long countByExample(DemoExample example);

    int deleteByExample(DemoExample example);

    int insert(DemoEntity record);

    int insertSelective(DemoEntity record);

    List<DemoEntity> selectByExample(DemoExample example);

    int updateByExampleSelective(@Param("record") DemoEntity record, @Param("example") DemoExample example);

    int updateByExample(@Param("record") DemoEntity record, @Param("example") DemoExample example);
}
