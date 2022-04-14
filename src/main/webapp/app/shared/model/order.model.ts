import { IClient } from 'app/shared/model/client.model';
import { ICourier } from 'app/shared/model/courier.model';
import { IProduct } from 'app/shared/model/product.model';

export interface IOrder {
  id?: string;
  price?: number;
  client?: IClient | null;
  courier?: ICourier | null;
  products?: IProduct[] | null;
}

export const defaultValue: Readonly<IOrder> = {};
