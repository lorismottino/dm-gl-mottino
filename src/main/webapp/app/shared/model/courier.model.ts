import { IOrder } from 'app/shared/model/order.model';
import { ICooperative } from 'app/shared/model/cooperative.model';

export interface ICourier {
  id?: string;
  firstname?: string;
  lastname?: string;
  phone?: string;
  email?: string | null;
  orders?: IOrder[] | null;
  cooperative?: ICooperative | null;
}

export const defaultValue: Readonly<ICourier> = {};
