import { post, get } from '@/utils/request'
import type { LoginRequest, LoginResponse, User, Result } from '@/types'

export function login(params: LoginRequest): Promise<Result<LoginResponse>> {
  return post<LoginResponse>('/auth/login', params)
}

export function logout(): Promise<Result<void>> {
  return post<void>('/auth/logout')
}

export function getUserInfo(): Promise<Result<User>> {
  return get<User>('/auth/userinfo')
}

export function refreshToken(): Promise<Result<{ token: string }>> {
  return post<{ token: string }>('/auth/refresh')
}
