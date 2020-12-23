export interface CardApplicationModel {
  id: string;
  name: string;
  description: string;
  photoUrl: string;
  location: string;
  createdBy: string;
  needsToBeChecked: boolean;
  uncommonness?: string;
  difficulty?: string;
  population?: string;
  createdTime?: string;
}
