package com.demo.app.infrastructure.repository.card.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.app.infrastructure.repository.card.CardDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * The interface Card mapper.
 */
@Mapper
public interface CardMapper extends BaseMapper<CardDO> {
}
