package com.helo.ming.yu.dao;

import com.helo.ming.yu.model.HaloBlog;
import org.springframework.stereotype.Repository;

@Repository
public interface HaloBlogDao {
    HaloBlog findByCheckSum(String checkSum);

    void save(HaloBlog blog);
}
