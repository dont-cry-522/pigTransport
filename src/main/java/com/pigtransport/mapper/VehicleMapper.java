package com.pigtransport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pigtransport.entity.Vehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {

    // 根据车牌号查询
    @Select("SELECT * FROM vehicle WHERE license_plate = #{licensePlate} AND deleted = 0")
    Vehicle selectByLicensePlate(@Param("licensePlate") String licensePlate);

    // 根据状态查询车辆
    @Select("SELECT * FROM vehicle WHERE status = #{status} AND deleted = 0 ORDER BY create_time DESC")
    List<Vehicle> selectByStatus(@Param("status") String status);

    // 查询空闲车辆（用于下拉框）
    @Select("SELECT vehicle_id, license_plate, model FROM vehicle WHERE status = 'free' AND deleted = 0")
    List<Vehicle> selectFreeVehicles();

    // 更新车辆状态（修正注解为@Update）
    @Update("UPDATE vehicle SET status = #{status} WHERE vehicle_id = #{vehicleId} AND deleted = 0")
    int updateStatus(@Param("vehicleId") Integer vehicleId, @Param("status") String status);

    // 新增：查询车辆详情（仅查vehicle表，无关联）
    @Select("SELECT * FROM vehicle WHERE vehicle_id = #{vehicleId} AND deleted = 0")
    Vehicle selectDetailById(@Param("vehicleId") Integer vehicleId);

    // 新增：带筛选的列表查询（仅查vehicle表，无关联）
    @Select("<script>" +
            "SELECT * FROM vehicle WHERE deleted = 0 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "  AND (license_plate LIKE CONCAT('%', #{keyword}, '%') OR model LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "<if test='status != null and status != \"\"'>" +
            "  AND status = #{status}" +
            "</if>" +
            "ORDER BY create_time DESC" +
            "</script>")
    List<Vehicle> selectByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status);
}