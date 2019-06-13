package org.devgateway.toolkit.persistence.service;

import org.devgateway.ocds.persistence.dao.ColorIndicatorPair;

public interface ColorIndicatorPairService extends BaseJpaService<ColorIndicatorPair>, TextSearchableService<ColorIndicatorPair> {

    ColorIndicatorPair findByFirstIndicatorAndSecondIndicator(String firstIndicator,
                                                               String secondIndicator);

}
