export type Car = {
  id: number;
  model: string;
  manufacturer: string;
  state: string;
}
export const CARS: Car[] = [
	{id: 1, model: 'BMW', state: 'good', manufacturer: '125$'},
	{id: 2, model: 'Audi', state: 'good', manufacturer: '115$'},
	{id: 3, model: 'Toyota', state: 'perfect', manufacturer: '89$'},
	{id: 4, model: 'Ford', state: 'good', manufacturer: '57$'},
	{id: 5, model: 'Mazda', state: 'good', manufacturer: '64$'},
  {id: 6, model: 'Volkswagen', state: 'bad', manufacturer: '49$'},
  {id: 7, model: 'Mitsubishi', state: 'perfect', manufacturer: '75$'},
  {id: 8, model: 'Renault', state: 'bad', manufacturer: '40$'},
];

