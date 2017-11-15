import cn from 'classnames';
import { Map } from 'immutable';
import translatable from '../translatable';
import TotalFlags from './total-flags';
import { CORRUPTION_TYPES } from './constants';

// eslint-disable-next-line no-undef
class Sidebar extends translatable(React.PureComponent) {
  render() {
    const { page, indicatorTypesMapping, filters, years, monthly, months, navigate, translations,
      data, requestNewData, route } = this.props;

    return (
      <aside className="col-xs-4 col-md-4 col-lg-3" id="crd-sidebar">
        <section role="navigation" className="row">
          <a
            href="javascript:void(0);"
            onClick={() => navigate()}
            className={cn('crd-description-link', { active: !page })}
          >
            <img className="blue" src="assets/icons/blue/overview.svg" alt="Overview icon" />
            <img className="white" src="assets/icons/white/overview.svg" alt="Overview icon" />
            {this.t('tabs:overview:title')}
            <i className="glyphicon glyphicon-info-sign" />
          </a>

          <p className="crd-description small">
            {this.t('crd:description')}
          </p>

          {CORRUPTION_TYPES.map((slug) => {
            const count = Object.keys(indicatorTypesMapping)
              .filter(key => indicatorTypesMapping[key].types.indexOf(slug) > -1)
              .length;

            let corruptionType;
            if (page === 'type') {
              [, corruptionType] = route;
            }

            return (
              <a
                href="javascript:void(0);"
                onClick={() => navigate('type', slug)}
                className={cn({ active: slug === corruptionType })}
                key={slug}
              >
                <img className="blue" src={`assets/icons/blue/${slug}.svg`} alt="Tab icon" />
                <img className="white" src={`assets/icons/white/${slug}.svg`} alt="Tab icon" />
                {this.t(`crd:corruptionType:${slug}:name`)} <span className="count">({count})</span>
              </a>
            );
          })}
          <a
            href="javascript:void(0);"
            onClick={() => navigate('suppliers')}
            className={cn('archive-link', { active: page === 'suppliers' })}
            key="suppliers"
          >
            <img className="blue" src={`assets/icons/blue/suppliers.svg`} alt="Suppliers icon" />
            <img className="white" src={`assets/icons/white/suppliers.svg`} alt="Suppliers icon" />
            {this.t('crd:contracts:baseInfo:suppliers')}
          </a>
          <a
            href="javascript:void(0);"
            onClick={() => navigate('procuring-entities')}
            className={cn('archive-link', { active: page === 'procuring-entities' })}
            key="procuring-entities"
          >
            <img className="blue" src={`assets/icons/blue/procuring-entities.svg`} alt="Procuring entities icon" />
            <img className="white" src={`assets/icons/white/procuring-entities.svg`} alt="Procuring entities icon" />
            {this.t('crd:contracts:menu:procuringEntities')}
          </a>
          <a
            href="javascript:void(0);"
            onClick={() => navigate('contracts')}
            className={cn('archive-link', 'contracts-link', { active: page === 'contracts' })}
            key="contracts"
          >
            <img className="blue" src={`assets/icons/blue/contracts.svg`} alt="Contracts icon" />
            <img className="white" src={`assets/icons/white/contracts.svg`} alt="Contracts icon" />
            {this.t('crd:general:contracts')}
          </a>
        </section>
        <TotalFlags
          filters={filters}
          requestNewData={(path, newData) => requestNewData(['totalFlags'].concat(path), newData)}
          translations={translations}
          data={data.get('totalFlags', Map())}
          years={years}
          months={months}
          monthly={monthly}
        />
      </aside>
    );
  }
}

export default Sidebar;
