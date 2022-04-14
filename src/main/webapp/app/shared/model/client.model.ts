import { IOrder } from 'app/shared/model/order.model';

export interface IClient {
  id?: string;
  firstname?: string;
  lastname?: string;
  address?: string;
  phone?: string | null;
  email?: string | null;
  orders?: IOrder[] | null;
}

export const defaultValue: Readonly<IClient> = {};
