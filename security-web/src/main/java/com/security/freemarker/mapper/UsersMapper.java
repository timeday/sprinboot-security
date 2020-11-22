package com.security.freemarker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.security.freemarker.dto.Users;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UsersMapper extends BaseMapper<Users> {
}
