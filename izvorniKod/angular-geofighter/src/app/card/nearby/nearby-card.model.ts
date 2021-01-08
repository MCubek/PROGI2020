export interface NearbyCardModel {
  id: string;
  name: string;
  description: string;
  photoURL: string;
  latitude: number;
  longitude: number;
  location: string;
  createdBy: string;
  needsToBeChecked: boolean;
  uncommonness?: string;
  difficulty?: string;
  population?: string;
  createdTime?: string;
}
