import { IOrder } from 'app/shared/model/order.model';
import { IRestaurant } from 'app/shared/model/restaurant.model';

export interface IProduct {
  id?: string;
  name?: string;
  price?: number;
  description?: string | null;
  orders?: IOrder[] | null;
  restaurant?: IRestaurant | null;
}

export const defaultValue: Readonly<IProduct> = {};
