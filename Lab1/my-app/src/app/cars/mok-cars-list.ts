import { Cars } from './cars';
export type Car = {
  id: number;
  model: string;
  state: string;
  price: string;
}
export const CARS: Car[] = [
	{id: 1, model: 'BMW', state: 'good', price: '125$'},
	{id: 2, model: 'Audi', state: 'good', price: '115$'},
	{id: 3, model: 'Toyota', state: 'perfect', price: '89$'},
	{id: 4, model: 'Ford', state: 'good', price: '57$'},
	{id: 5, model: 'Mazda', state: 'good', price: '64$'},
  {id: 6, model: 'Volkswagen', state: 'bad', price: '49$'},
  {id: 7, model: 'Mitsubishi', state: 'perfect', price: '75$'},
  {id: 8, model: 'Renault', state: 'bad', price: '40$'},
];

