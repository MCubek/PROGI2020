import { URL } from 'url';

export interface ApplyCardRequestPayload {
  id: string;
  name: string;
  description: string;
  photoUrl: string;
  location: string;
  createdBy: string;
}
