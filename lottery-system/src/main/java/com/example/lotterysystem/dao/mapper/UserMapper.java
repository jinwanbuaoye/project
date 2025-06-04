package com.example.lotterysystem.dao.mapper;

import com.example.lotterysystem.dao.dataobject.Encrypt;
import com.example.lotterysystem.dao.dataobject.UserDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 查询邮箱绑定人数
     * @param email
     * @return
     */
    @Select("select count(*) from user where email = #{email}")
    int countByMail(@Param("email") String email);

    @Select("select count(*) from user where phone_number = #{phoneNumber}")
    int countByPhoneNumber(@Param("phoneNumber") Encrypt phoneNumber);

    @Insert("insert into user (user_name, email, phone_number, password, identity)" +
            " values (#{userName}, #{email}, #{phoneNumber}, #{password}, #{identity})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(UserDO userDO);

    @Select("select id, gmt_create, gmt_modified,"
            + " user_name, email, phone_number, password, identity"
            + " from user where email = #{email}")
    UserDO selectByMail(String loginName);

    /**
     * 通过手机号查询用户信息
     *
     * @param phoneNumber
     * @return
     */
    @Select("select id, gmt_create, gmt_modified," +
            " user_name, email, phone_number, password, identity" +
            " from user where phone_number = #{phoneNumber}")
    UserDO selectByPhoneNumber(@Param("phoneNumber") Encrypt phoneNumber);

    /**
     * 根据身份查用户
     * @param identity
     * @return
     */
    @Select("<script>" +
            " select * from user" +
            " <if test=\"identity!=null\">" +
            "    where identity = #{identity}" +
            " </if>" +
            " order by id desc" +
            " </script>")
    List<UserDO> selectUserListByIdentity(@Param("identity")String identity);

    @Select("<script>" +
            " select id from user" +
            " where id in" +
            " <foreach item='item' collection='items' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            " </script>")
    List<Long> selectExistByIds(@Param("items") List<Long> ids);

    @Select("<script>" +
            " select * from user" +
            " where id in" +
            " <foreach item='item' collection='items' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            " </script>")
    List<UserDO> batchSelectByIds(@Param("items") List<Long> ids);
}
