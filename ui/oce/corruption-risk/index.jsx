import cn from 'classnames';
import URI from 'urijs';
import { Map, Set } from 'immutable';
import PropTypes from 'prop-types';
import { fetchJson, debounce, cacheFn, range, pluck, callFunc } from '../tools';
import OverviewPage from './overview-page';
import CorruptionTypePage from './corruption-type';
import IndividualIndicatorPage from './individual-indicator';
import ContractsPage from './contracts';
import ContractPage from './contracts/single';
import SuppliersPage from './suppliers';
import SupplierPage from './suppliers/single';
import ProcuringEntitiesPage from './procuring-entities';
import ProcuringEntityPage from './procuring-entities/single';
import Filters from './filters';
import LandingPopup from './landing-popup';
import { LOGIN_URL } from './constants';
// eslint-disable-next-line no-unused-vars
import style from './style.less';
import Sidebar from './sidebar';
import { filters as CRDFilters } from '../state/oce-state';

// eslint-disable-next-line no-undef
class CorruptionRiskDashboard extends React.Component {
  constructor(...args) {
    super(...args);
    this.state = {
      dashboardSwitcherOpen: false,
      user: {
        loggedIn: false,
        isAdmin: false,
      },
      indicatorTypesMapping: {},
      currentFiltersState: Map(),
      appliedFilters: Map(),
      filterBoxIndex: null,
      allMonths: range(1, 12),
      allYears: [],
      width: 0,
      data: Map(),
      showLandingPopup: !localStorage.alreadyVisited,
    };
    const { oceLocale } = localStorage;
    if (oceLocale && this.constructor.TRANSLATIONS[oceLocale]) {
      this.state.locale = oceLocale;
    } else {
      this.state.locale = 'en_US';
    }

    localStorage.alreadyVisited = true;

    this.destructFilters = cacheFn(filters => ({
      filters: filters.delete('years').delete('months'),
      years: filters.get('years', Set()),
      months: filters.get('months', Set()),
    }));
  }

  componentDidMount() {
    this.fetchUserInfo();
    this.fetchIndicatorTypesMapping();
    this.fetchYears();

    // eslint-disable-next-line react/no-did-mount-set-state
    this.setState({
      width: document.querySelector('.content').offsetWidth - 30,
    });

    window.addEventListener('resize', debounce(() => {
      this.setState({
        width: document.querySelector('.content').offsetWidth - 30,
      });
    }));
  }

  setLocale(locale) {
    this.setState({ locale });
    localStorage.oceLocale = locale;
  }

  getPage() {
    const { route, navigate } = this.props;
    const styling = this.constructor.STYLING || this.props.styling;
    const [page] = route;

    const { indicatorTypesMapping, width } = this.state;

    if (page === 'type') {
      const [, corruptionType] = route;

      const indicators =
        Object.keys(indicatorTypesMapping).filter(key =>
          indicatorTypesMapping[key].types.indexOf(corruptionType) > -1);

      return (
        <CorruptionTypePage
          {...this.wireProps(['corruptionType', corruptionType])}
          indicators={indicators}
          onGotoIndicator={individualIndicator => navigate('indicator', corruptionType, individualIndicator)}
          corruptionType={corruptionType}
          width={width}
          styling={styling}
        />
      );
    } else if (page === 'indicator') {
      const [, corruptionType, individualIndicator] = route;
      return (
        <IndividualIndicatorPage
          {...this.wireProps(['indicator', individualIndicator])}
          indicator={individualIndicator}
          corruptionType={corruptionType}
          width={width}
          styling={styling}
          navigate={navigate}
        />
      );
    } else if (page === 'contracts') {
      return this.renderArchive(ContractsPage, 'contracts');
    } else if (page === 'contract') {
      return this.renderSingle({
        Component: ContractPage,
        sgSlug: 'contract',
        plSlug: 'contracts',
        additionalProps: {
          totalContracts: this.state.data.getIn(['totalFlags', 'contractCounter'], 0),
        },
      });
    } else if (page === 'suppliers') {
      return this.renderArchive(SuppliersPage, 'suppliers');
    } else if (page === 'supplier') {
      return this.renderSingle({
        Component: SupplierPage,
        sgSlug: 'supplier',
        plSlug: 'suppliers',
      });
    } else if (page === 'procuring-entities') {
      return this.renderArchive(ProcuringEntitiesPage, 'procuring-entities');
    } else if (page === 'procuring-entity') {
      return this.renderSingle({
        Component: ProcuringEntityPage,
        sgSlug: 'procuring-entity',
        plSlug: 'procuring-entities',
      });
    }
    return (
      <OverviewPage
        {...this.wireProps('overview')}
        indicatorTypesMapping={indicatorTypesMapping}
        styling={styling}
        width={width}
        navigate={navigate}
      />
    );
  }

  getTranslations() {
    const { TRANSLATIONS } = this.constructor;
    const { locale } = this.state;
    return TRANSLATIONS[locale];
  }

  wireProps(_slug) {
    const slug = Array.isArray(_slug) ? _slug : [_slug];
    const translations = this.getTranslations();
    const { appliedFilters, allYears, width } = this.state;
    const { filters, years: selectedYears, months } = this.destructFilters(appliedFilters);
    const years = Set(allYears).equals(selectedYears) ?
      Set() :
      selectedYears;

    return {
      translations,
      data: this.state.data.getIn(slug, Map()),
      requestNewData: (path, newData) =>
        this.setState({ data: this.state.data.setIn(slug.concat(path), newData) }),
      filters,
      years,
      monthly: years.count() === 1,
      months,
      width,
    };
  }

  t(str) {
    const { locale } = this.state;
    const { TRANSLATIONS } = this.constructor;
    return TRANSLATIONS[locale][str] || TRANSLATIONS.en_US[str] || str;
  }

  fetchUserInfo() {
    const noCacheUrl = new URI('/isAuthenticated').addSearch('time', Date.now());
    fetchJson(noCacheUrl).then(({ authenticated, disabledApiSecurity }) => {
      this.setState({
        user: {
          loggedIn: authenticated,
        },
        showLandingPopup: !authenticated || disabledApiSecurity,
        disabledApiSecurity,
      });
    });
  }

  fetchIndicatorTypesMapping() {
    fetchJson('/api/indicatorTypesMapping').then(data => this.setState({ indicatorTypesMapping: data }));
  }

  fetchYears() {
    fetchJson('/api/tendersAwardsYears').then((data) => {
      const years = data.map(pluck('_id'));
      const { allMonths, currentFiltersState, appliedFilters } = this.state;
      this.setState({
        currentFiltersState: currentFiltersState
          .set('years', Set(years))
          .set('months', Set(allMonths)),
        appliedFilters: appliedFilters
          .set('years', Set(years))
          .set('months', Set(allMonths)),
        allYears: years,
      });
    });
  }

  toggleDashboardSwitcher(e) {
    e.stopPropagation();
    const { dashboardSwitcherOpen } = this.state;
    this.setState({ dashboardSwitcherOpen: !dashboardSwitcherOpen });
  }

  loginBox() {
    if (this.state.user.loggedIn) {
      return (
        <a href="/preLogout?referrer=/ui/index.html?corruption-risk-dashboard">
          <button className="btn btn-success">
            {this.t('general:logout')}
          </button>
        </a>
      );
    }
    const hash = encodeURIComponent(location.hash);
    return (
      <a href={`${LOGIN_URL}${hash}`}>
        <button className="btn btn-success">
          {this.t('general:login')}
        </button>
      </a>
    );
  }

  languageSwitcher() {
    const { TRANSLATIONS } = this.constructor;
    const { locale: selectedLocale } = this.state;
    if (Object.keys(TRANSLATIONS).length <= 1) return null;
    return Object.keys(TRANSLATIONS).map(locale => (
      <a
        href="javascript:void(0);"
        onClick={() => this.setLocale(locale)}
        className={cn({ active: locale === selectedLocale })}
      >
        {locale.split('_')[0]}
      </a>
    ));
  }

  renderArchive(Component, slug) {
    const { navigate, route } = this.props;
    const [, searchQuery] = route;
    return (
      <Component
        {...this.wireProps(slug)}
        searchQuery={searchQuery}
        doSearch={query => navigate(slug, query)}
        navigate={navigate}
      />
    );
  }

  renderSingle({ Component, sgSlug, plSlug, additionalProps }) {
    const { route, navigate, styling } = this.props;
    const { indicatorTypesMapping } = this.state;
    const [, id] = route;
    return (
      <Component
        {...this.wireProps(sgSlug)}
        id={id}
        styling={styling}
        doSearch={query => navigate(plSlug, query)}
        indicatorTypesMapping={indicatorTypesMapping}
        {...additionalProps}
      />
    );
  }

  render() {
    const { dashboardSwitcherOpen, filterBoxIndex, currentFiltersState, appliedFilters, data,
      indicatorTypesMapping, allYears, allMonths, showLandingPopup,
      disabledApiSecurity } = this.state;

    const { onSwitch, route, navigate } = this.props;
    const translations = this.getTranslations();
    const [page] = route;

    return (
      <div
        className="container-fluid dashboard-corruption-risk"
        onMouseDown={() => this.setState({ dashboardSwitcherOpen: false, filterBoxIndex: null })}
      >
        {showLandingPopup &&
          <LandingPopup
            redirectToLogin={!disabledApiSecurity}
            requestClosing={() => this.setState({ showLandingPopup: false })}
            translations={translations}
            languageSwitcher={(...args) => this.languageSwitcher(...args)}
          />
        }
        <header className="branding row">
          <div className="col-sm-9 logo-wrapper">
            <img src="assets/dg-logo.svg" alt="DG logo" />
            <div className={cn('dash-switcher-wrapper', { open: dashboardSwitcherOpen })}>
              <h1
                className="corruption-dash-title"
                onClick={e => this.toggleDashboardSwitcher(e)}
              >
                {this.t('crd:title')}
                <i className="glyphicon glyphicon-menu-down" />
              </h1>
              {dashboardSwitcherOpen &&
                <div className="dashboard-switcher">
                  <a
                    href="javascript:void(0);"
                    onClick={() => onSwitch('m-and-e')}
                    onMouseDown={callFunc('stopPropagation')}
                  >
                    M&E Toolkit
                  </a>
                </div>
              }
            </div>
          </div>
          <div className="col-sm-1 language-switcher">
            {this.languageSwitcher()}
          </div>
          <div className="col-sm-1 login-wrapper">
            {!disabledApiSecurity && this.loginBox()}
          </div>
          <div className="col-sm-1" />
        </header>
        <Filters
          onUpdate={newState => this.setState({ currentFiltersState: newState })}
          onApply={filtersToApply => {
              CRDFilters.assign('CRD Dash', filtersToApply);
              this.setState({
                filterBoxIndex: null,
                appliedFilters: filtersToApply,
                currentFiltersState: filtersToApply,
              })
          }}
          translations={translations}
          currentBoxIndex={filterBoxIndex}
          requestNewBox={index => this.setState({ filterBoxIndex: index })}
          state={currentFiltersState}
          appliedFilters={appliedFilters}
          allYears={allYears}
          allMonths={allMonths}
        />
        <Sidebar
          {...this.wireProps('sidebar')}
          page={page}
          indicatorTypesMapping={indicatorTypesMapping}
          route={route}
          navigate={navigate}
          data={data}
          requestNewData={(path, newData) =>
            this.setState({ data: this.state.data.setIn(path, newData) })}
          allYears={allYears}
        />
        <div className="col-sm-offset-3 col-sm-9 content">
          {this.getPage()}
        </div>
      </div>
    );
  }
}

CorruptionRiskDashboard.propTypes = {
  translations: PropTypes.object.isRequired,
  styling: PropTypes.object.isRequired,
  onSwitch: PropTypes.func.isRequired,
  route: PropTypes.array.isRequired,
  navigate: PropTypes.func.isRequired
};

CorruptionRiskDashboard.TRANSLATIONS = {
  en_US: require('../../../web/public/languages/en_US.json'),
  es_ES: require('../../../web/public/languages/es_ES.json'),
  fr_FR: require('../../../web/public/languages/fr_FR.json'),
};

export default CorruptionRiskDashboard;
