import cn from 'classnames';
import CRDPage from './page';
import TopSearch from './top-search';
import { wireProps } from './tools';

class Archive extends CRDPage {
  render() {
    const { className, searchQuery, doSearch, topSearchPlaceholder, translations, data, List, dataEP
      , countEP, navigate } = this.props;

    const count = data.get('count');

    return (
      <div className={cn(className)}>
        <TopSearch
          translations={translations}
          searchQuery={searchQuery ? decodeURI(searchQuery) : ''}
          doSearch={doSearch}
          placeholder={topSearchPlaceholder}
        />

        {searchQuery && <h3 className="page-header">
          {
            (count === 1 ?
              this.t('crd:contracts:top-search:resultsFor:sg') :
              this.t('crd:contracts:top-search:resultsFor:pl')
            ).replace('$#$', count).replace(
              '$#$',
              searchQuery.replace(/\%22/g, '')
            )}
        </h3>}

        <List
          {...wireProps(this)}
          dataEP={dataEP}
          countEP={countEP}
          searchQuery={searchQuery}
          navigate={navigate}
        />
      </div>
    );
  }
}

export default Archive;
