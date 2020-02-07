package com.tax.verify.jpa;

import com.tax.verify.dto.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("indexService")
public class IndexServiceImp implements IndexService {
    @Autowired
    private IndexRepository indexRepository;


    @Override
    public Data save(Data data) {
        return indexRepository.save(data);
    }
}
