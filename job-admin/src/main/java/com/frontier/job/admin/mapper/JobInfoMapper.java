package com.frontier.job.admin.mapper;

import com.frontier.job.admin.model.JobInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface JobInfoMapper {
    List<JobInfo> listPage(JobInfo jobInfo, RowBounds rowBounds);

    List<JobInfo> listLikePage(JobInfo jobInfo, RowBounds rowBounds);

    List<JobInfo> list(JobInfo JobInfo);

    JobInfo getById(Long id);

    JobInfo getOne(JobInfo jobInfo);

    Long count(JobInfo JobInfo);

    Long countLike(JobInfo JobInfo);

    void save(JobInfo JobInfo);

    void saveBatch(List<JobInfo> list);

    int update(JobInfo JobInfo);
    int updateById(JobInfo JobInfo);

    void updateBatch(List<JobInfo> list);

    void delete(Long id);
}