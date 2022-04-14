import cooperative from 'app/entities/cooperative/cooperative.reducer';
import client from 'app/entities/client/client.reducer';
import courier from 'app/entities/courier/courier.reducer';
import restaurant from 'app/entities/restaurant/restaurant.reducer';
import product from 'app/entities/product/product.reducer';
import order from 'app/entities/order/order.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  cooperative,
  client,
  courier,
  restaurant,
  product,
  order,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
