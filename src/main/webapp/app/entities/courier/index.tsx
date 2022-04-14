import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Courier from './courier';
import CourierDetail from './courier-detail';
import CourierUpdate from './courier-update';
import CourierDeleteDialog from './courier-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CourierUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CourierUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CourierDetail} />
      <ErrorBoundaryRoute path={match.url} component={Courier} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CourierDeleteDialog} />
  </>
);

export default Routes;
