import { ICourier } from 'app/shared/model/courier.model';
import { IRestaurant } from 'app/shared/model/restaurant.model';

export interface ICooperative {
  id?: string;
  name?: string;
  couriers?: ICourier[] | null;
  restaurants?: IRestaurant[] | null;
}

export const defaultValue: Readonly<ICooperative> = {};
