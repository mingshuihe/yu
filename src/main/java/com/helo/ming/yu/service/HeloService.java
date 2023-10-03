package com.helo.ming.yu.service;

import com.helo.ming.yu.dao.HaloBlogDao;
import com.helo.ming.yu.model.HaloBlog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeloService {
    @Autowired
    HaloBlogDao haloBlogDao;

    public HaloBlog findByCheckSum(String checkSum) {
        return haloBlogDao.findByCheckSum(checkSum);
    }

    public void save(HaloBlog blog) {
        haloBlogDao.save(blog);
    }
}
