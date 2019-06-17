package org.devgateway.toolkit.persistence.service;

import org.devgateway.ocds.persistence.dao.ColorIndicatorPair;
import org.devgateway.ocds.persistence.repository.ColorIndicatorPairRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColorIndicatorPairServiceImpl extends BaseJpaServiceImpl<ColorIndicatorPair>
        implements ColorIndicatorPairService {

    @Autowired
    private ColorIndicatorPairRepository repository;

    @Override
    protected BaseJpaRepository<ColorIndicatorPair, Long> repository() {
        return repository;
    }

    @Override
    public ColorIndicatorPair newInstance() {
        return new ColorIndicatorPair();
    }

    @Override
    public TextSearchableRepository<ColorIndicatorPair, Long> textRepository() {
        return repository;
    }

    @Override
    public ColorIndicatorPair findByFirstIndicatorAndSecondIndicator(String firstIndicator, String secondIndicator) {
        return repository.findByFirstIndicatorAndSecondIndicator(firstIndicator, secondIndicator);
    }
}
