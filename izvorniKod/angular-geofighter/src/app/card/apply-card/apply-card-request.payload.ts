import { URL } from 'url';

export interface ApplyCardRequestPayload {
  name: string;
  description: string;
  photoUrl: string;
  location: string;
  createdBy: string;
}
