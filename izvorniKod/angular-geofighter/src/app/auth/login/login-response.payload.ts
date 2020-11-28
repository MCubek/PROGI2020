export interface LoginResponsePayload {
  authorizationToken: string;
  refreshToken: string;
  expiresAt: Date;
  username: string;
  role: string;
}
