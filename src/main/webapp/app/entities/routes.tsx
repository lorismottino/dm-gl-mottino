import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cooperative from './cooperative';
import Client from './client';
import Courier from './courier';
import Restaurant from './restaurant';
import Product from './product';
import Order from './order';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}cooperative`} component={Cooperative} />
        <ErrorBoundaryRoute path={`${match.url}client`} component={Client} />
        <ErrorBoundaryRoute path={`${match.url}courier`} component={Courier} />
        <ErrorBoundaryRoute path={`${match.url}restaurant`} component={Restaurant} />
        <ErrorBoundaryRoute path={`${match.url}product`} component={Product} />
        <ErrorBoundaryRoute path={`${match.url}order`} component={Order} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
