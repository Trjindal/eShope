package com.eshope.admin.Service;

import com.eShope.common.entity.Section.Section;
import com.eshope.admin.Exception.SectionUnmoveableException;
import com.eshope.admin.Repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    public List<Section> listSections() {
        return sectionRepository.findAllSectionsSortedByOrder();
    }

    public void saveSection(Section section) {
        if (section.getId() == null) {
            setPositionForNewSection(section);
        }
        sectionRepository.save(section);
    }

    private void setPositionForNewSection(Section section) {
        Long newPosition = sectionRepository.count() + 1;
        section.setSectionOrder(newPosition.intValue());
    }

    public Section getSection(Integer id) throws UsernameNotFoundException {
        try {
            return sectionRepository.findById(id).get();
        } catch (UsernameNotFoundException ex) {
            throw new UsernameNotFoundException("Could not find any section with ID " + id);
        }
    }

    public void deleteSection(Integer id) throws UsernameNotFoundException {
        if (!sectionRepository.existsById(id)) {
            throw new UsernameNotFoundException("Could not find any section with ID " + id);
        }

        sectionRepository.deleteById(id);
    }

    public void updateSectionEnabledStatus(Integer id, boolean enabled)
            throws UsernameNotFoundException {
        if (!sectionRepository.existsById(id)) {
            throw new UsernameNotFoundException("Could not find any section with ID " + id);
        }

        sectionRepository.updateEnabledStatus(id, enabled);
    }

    public void moveSectionUp(Integer id) throws UsernameNotFoundException, SectionUnmoveableException {
        Section currentSection = sectionRepository.getSimpleSectionById(id);
        if (currentSection == null) {
            throw new UsernameNotFoundException("Could not find any section with ID " + id);
        }

        List<Section> allSections = sectionRepository.getOnlySectionIDsSortedByOrder();

        int currentSectionIndex = allSections.indexOf(currentSection);
        if (currentSectionIndex == 0) {
            throw new SectionUnmoveableException("The section ID " + id + " is already in the first position");
        }

        int previousSectionIndex = currentSectionIndex - 1;
        Section previousSection = allSections.get(previousSectionIndex);

        currentSection.setSectionOrder(previousSectionIndex + 1);
        previousSection.setSectionOrder(currentSectionIndex + 1);

        sectionRepository.updateSectionPosition(currentSection.getSectionOrder(), currentSection.getId());
        sectionRepository.updateSectionPosition(previousSection.getSectionOrder(), previousSection.getId());
    }

    public void moveSectionDown(Integer id) throws UsernameNotFoundException, SectionUnmoveableException {
        Section currentSection = sectionRepository.getSimpleSectionById(id);
        if (currentSection == null) {
            throw new UsernameNotFoundException("Could not find any section with ID " + id);
        }

        List<Section> allSections = sectionRepository.getOnlySectionIDsSortedByOrder();

        int currentSectionIndex = allSections.indexOf(currentSection);
        if (currentSectionIndex == allSections.size() - 1) {
            throw new SectionUnmoveableException("The section ID " + id + " is already in the last position");
        }

        int nextSectionIndex = currentSectionIndex + 1;
        Section nextSection = allSections.get(nextSectionIndex);

        currentSection.setSectionOrder(nextSectionIndex + 1);
        nextSection.setSectionOrder(currentSectionIndex + 1);

        sectionRepository.updateSectionPosition(currentSection.getSectionOrder(), currentSection.getId());
        sectionRepository.updateSectionPosition(nextSection.getSectionOrder(), nextSection.getId());
    }
}