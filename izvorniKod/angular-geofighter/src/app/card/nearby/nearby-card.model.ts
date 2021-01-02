export interface NearbyCardModel {
  id: string;
  name: string;
  description: string;
  photoUrl: string;
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
