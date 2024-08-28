import {inject, Injectable} from '@angular/core';
import {Car, CARS} from "./mok-cars-list";
import {
  Firestore, doc, getDoc, getDocs, addDoc, setDoc,
  updateDoc, collectionGroup, collection, collectionData, orderBy,
  query, where, Query, deleteDoc
} from '@angular/fire/firestore';
@Injectable({
  providedIn: 'root'
})
export class CarsService {

  private querySnapshot : any;
  private dbPath = 'list-cars';
  constructor(private db: Firestore) {
    this.db=inject(Firestore);
  }
  async getCars(){
    const q = query(collection(this.db, this.dbPath),
      orderBy("id"));
    const querySnapshot = await getDocs(q);

    const carList=querySnapshot.docs.map(doc => doc.data());
    return carList;
  }
    async getCarData(id: number): Promise<Car | null> {
    const q = query(collection(this.db, this.dbPath), where("id", "==", id));
    const querySnapshot = await getDocs(q);
    if (!querySnapshot.empty) {
      const carDoc = querySnapshot.docs[0];
      return {
        id: carDoc.data()['id'],
        model: carDoc.data()['model'],
        state: carDoc.data()['state'],
        price: carDoc.data()['price']
      } as Car;
    } else {
      //throw new Error('Car with id ' + id + ' does not exist.');
      return null;
    }
  }



  async addCar(model: string, state: string, price: string) {
    const newId = await this.generateUniqueId();
    let car : Car = {
      id: newId,
      model : model,
      state: state,
      price: price
    };
    addDoc(collection(this.db, this.dbPath), Object.assign({}, car));
  }

  async editCar(id: number, model: string, state: string, price: string) {
    let modelUpdate: string='';
    let idUpdate: number | string;
    idUpdate=id;
    console.log(idUpdate);
    const q = query(collection(this.db, this.dbPath), where('id', '==', idUpdate));
    const querySnapshot = await getDocs(q);
    querySnapshot.forEach((d) => {
      modelUpdate=d.id;
    })
    const newDoc=doc(this.db, this.dbPath, modelUpdate);
    await setDoc(newDoc, {model: model, state: state, price: price},{merge: true});
    //return setDoc(doc(this.db, this.dbPath, car.id), developer);
  }

  async deleteCar(id: number) {
    const q = query(collection(this.db, this.dbPath),
      where('id', '==', id));
    this.querySnapshot = await getDocs(q);
    this.querySnapshot.forEach((docElement: any) => {
      //console.log(docElement.id, ' => ', docElement.data());
      deleteDoc(doc(this.db, this.dbPath, docElement.id));
    });

  }

  async generateUniqueId(): Promise<number> {
    const carList = await this.getCars();
    const maxId = Math.max(...carList.map(car => car['id']));
    return maxId + 1;
  }
}
