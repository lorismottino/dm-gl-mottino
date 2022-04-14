import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICooperative } from 'app/shared/model/cooperative.model';
import { getEntities as getCooperatives } from 'app/entities/cooperative/cooperative.reducer';
import { ICourier } from 'app/shared/model/courier.model';
import { getEntity, updateEntity, createEntity, reset } from './courier.reducer';

export const CourierUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const cooperatives = useAppSelector(state => state.cooperative.entities);
  const courierEntity = useAppSelector(state => state.courier.entity);
  const loading = useAppSelector(state => state.courier.loading);
  const updating = useAppSelector(state => state.courier.updating);
  const updateSuccess = useAppSelector(state => state.courier.updateSuccess);
  const handleClose = () => {
    props.history.push('/courier');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCooperatives({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...courierEntity,
      ...values,
      cooperative: cooperatives.find(it => it.id.toString() === values.cooperative.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...courierEntity,
          cooperative: courierEntity?.cooperative?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopcycleApp.courier.home.createOrEditLabel" data-cy="CourierCreateUpdateHeading">
            <Translate contentKey="coopcycleApp.courier.home.createOrEditLabel">Create or edit a Courier</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="courier-id"
                  label={translate('coopcycleApp.courier.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('coopcycleApp.courier.firstname')}
                id="courier-firstname"
                name="firstname"
                data-cy="firstname"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                  pattern: { value: /^[A-Za-z\- ]+$/, message: translate('entity.validation.pattern', { pattern: '^[A-Za-z\\- ]+$' }) },
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.courier.lastname')}
                id="courier-lastname"
                name="lastname"
                data-cy="lastname"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                  pattern: { value: /^[A-Za-z\- ]+$/, message: translate('entity.validation.pattern', { pattern: '^[A-Za-z\\- ]+$' }) },
                }}
              />
              <ValidatedField
                label={translate('coopcycleApp.courier.phone')}
                id="courier-phone"
                name="phone"
                data-cy="phone"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('coopcycleApp.courier.email')} id="courier-email" name="email" data-cy="email" type="text" />
              <ValidatedField
                id="courier-cooperative"
                name="cooperative"
                data-cy="cooperative"
                label={translate('coopcycleApp.courier.cooperative')}
                type="select"
              >
                <option value="" key="0" />
                {cooperatives
                  ? cooperatives.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/courier" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CourierUpdate;
