import { IProduct } from 'app/shared/model/product.model';
import { ICooperative } from 'app/shared/model/cooperative.model';

export interface IRestaurant {
  id?: string;
  name?: string;
  address?: string;
  products?: IProduct[] | null;
  cooperatives?: ICooperative[] | null;
}

export const defaultValue: Readonly<IRestaurant> = {};
