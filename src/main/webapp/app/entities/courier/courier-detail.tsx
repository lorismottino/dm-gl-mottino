import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './courier.reducer';

export const CourierDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const courierEntity = useAppSelector(state => state.courier.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="courierDetailsHeading">
          <Translate contentKey="coopcycleApp.courier.detail.title">Courier</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="coopcycleApp.courier.id">Id</Translate>
            </span>
          </dt>
          <dd>{courierEntity.id}</dd>
          <dt>
            <span id="firstname">
              <Translate contentKey="coopcycleApp.courier.firstname">Firstname</Translate>
            </span>
          </dt>
          <dd>{courierEntity.firstname}</dd>
          <dt>
            <span id="lastname">
              <Translate contentKey="coopcycleApp.courier.lastname">Lastname</Translate>
            </span>
          </dt>
          <dd>{courierEntity.lastname}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="coopcycleApp.courier.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{courierEntity.phone}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="coopcycleApp.courier.email">Email</Translate>
            </span>
          </dt>
          <dd>{courierEntity.email}</dd>
          <dt>
            <Translate contentKey="coopcycleApp.courier.cooperative">Cooperative</Translate>
          </dt>
          <dd>{courierEntity.cooperative ? courierEntity.cooperative.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/courier" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/courier/${courierEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CourierDetail;
