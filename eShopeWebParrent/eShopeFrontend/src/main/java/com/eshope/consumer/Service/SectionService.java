package com.eshope.consumer.Service;

import java.util.List;

import com.eShope.common.entity.Section.Section;
import com.eshope.consumer.Repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class SectionService {
    @Autowired private SectionRepository repo;

    public List<Section> listEnabledSections() {
        return repo.findAllByEnabledOrderBySectionOrderAsc(true);
    }
}