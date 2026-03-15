package com.pigtransport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pigtransport.entity.Batch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface BatchMapper extends BaseMapper<Batch> {

    // 根据养殖户ID查询批次
    @Select("SELECT * FROM pig_batch WHERE breeder_id = #{breederId} AND deleted = 0 ORDER BY create_time DESC")
    List<Batch> selectByBreederId(@Param("breederId") Integer breederId);

    // 查询可运输的批次（用于任务分配）
    @Select("SELECT * FROM pig_batch WHERE status = 'available' AND deleted = 0 ORDER BY create_time DESC")
    List<Batch> selectAvailableBatches();

    // 更新批次状态
    @Select("UPDATE pig_batch SET status = #{status} WHERE batch_id = #{batchId}")
    int updateStatus(@Param("batchId") Integer batchId, @Param("status") String status);

    // 统计各养殖户的批次数量
    @Select("SELECT breeder_id, COUNT(*) as batch_count FROM pig_batch WHERE deleted = 0 GROUP BY breeder_id")
    List<Map<String, Object>> countByBreeder();
}