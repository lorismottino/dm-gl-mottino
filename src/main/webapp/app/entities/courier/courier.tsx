import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICourier } from 'app/shared/model/courier.model';
import { searchEntities, getEntities } from './courier.reducer';

export const Courier = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const courierList = useAppSelector(state => state.courier.entities);
  const loading = useAppSelector(state => state.courier.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const startSearching = e => {
    if (search) {
      dispatch(searchEntities({ query: search }));
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="courier-heading" data-cy="CourierHeading">
        <Translate contentKey="coopcycleApp.courier.home.title">Couriers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopcycleApp.courier.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/courier/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopcycleApp.courier.home.createLabel">Create new Courier</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('coopcycleApp.courier.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {courierList && courierList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="coopcycleApp.courier.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.courier.firstname">Firstname</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.courier.lastname">Lastname</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.courier.phone">Phone</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.courier.email">Email</Translate>
                </th>
                <th>
                  <Translate contentKey="coopcycleApp.courier.cooperative">Cooperative</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {courierList.map((courier, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/courier/${courier.id}`} color="link" size="sm">
                      {courier.id}
                    </Button>
                  </td>
                  <td>{courier.firstname}</td>
                  <td>{courier.lastname}</td>
                  <td>{courier.phone}</td>
                  <td>{courier.email}</td>
                  <td>{courier.cooperative ? <Link to={`/cooperative/${courier.cooperative.id}`}>{courier.cooperative.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/courier/${courier.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/courier/${courier.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/courier/${courier.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="coopcycleApp.courier.home.notFound">No Couriers found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Courier;
