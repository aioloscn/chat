package com.aiolos.utils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 继承自己的MyMapper
 * 该接口不能被扫描到，否则会出错
 * @author Aiolos
 * @date 2019-03-17 22:24
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
