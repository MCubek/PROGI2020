export interface SendRequestPayload{
  usernameSender: string;
  usernameReceiver: string;
  answer: boolean;
  battleId: number;
  seen: boolean;
}
