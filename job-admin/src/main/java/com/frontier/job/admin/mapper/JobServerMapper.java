package com.frontier.job.admin.mapper;

import com.frontier.job.admin.model.JobServer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface JobServerMapper {
    List<JobServer> listPage(JobServer jobServer, RowBounds rowBounds);

    List<JobServer> listLikePage(JobServer jobServer, RowBounds rowBounds);

    List<JobServer> list(JobServer JobServer);

    JobServer getById(int id);

    JobServer getOne(JobServer jobServer);

    Long count(JobServer JobServer);

    Long countLike(JobServer JobServer);

    int save(JobServer JobServer);

    void saveBatch(List<JobServer> list);

    // 更新服务状态
    int updateStatus(@Param("id") Integer id, @Param("status") byte status);

    int update(JobServer JobServer);

    void updateBatch(List<JobServer> list);

    void delete(Long id);
}