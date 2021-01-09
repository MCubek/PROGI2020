export interface CardModel {
  id: number;
  name: string;
  difficulty: number;
  population: number;
  uncommonness: number;
  photoURL?: string;
  cooldownMultiplier?: number;
}
